package com.uniplat.uniplatapi.extensions

import com.uniplat.uniplatapi.configuration.security.auth.JwtAuthenticationToken
import com.uniplat.uniplatapi.domain.enums.UserType
import com.uniplat.uniplatapi.domain.model.User
import com.uniplat.uniplatapi.exception.BadRequestException
import com.uniplat.uniplatapi.exception.UniplatException
import com.uniplat.uniplatapi.model.PaginatedModel
import com.uniplat.uniplatapi.model.PaginatedResponse
import io.jsonwebtoken.Claims
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import jakarta.validation.Validator
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.slf4j.LoggerFactory
import org.springframework.core.convert.ConversionService
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.r2dbc.convert.EnumWriteSupport
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.http.HttpStatusCode
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.util.Date
import java.util.UUID
import java.util.concurrent.TimeUnit

inline fun <reified T : Enum<T>?> enumConverterOf(): EnumWriteSupport<T> {
    return object : EnumWriteSupport<T>() {}
}

inline fun WebClient.Builder.configureTimeouts(
    connectionTimeout: Duration,
    readTimeout: Duration,
    httpClientCustomizer: HttpClient.() -> HttpClient = { this }
): WebClient.Builder {
    return clientConnector(
        ReactorClientHttpConnector(
            HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeout.toMillis().toInt())
                .doOnConnected { it.addHandlerLast(ReadTimeoutHandler(readTimeout.toMillis(), TimeUnit.MILLISECONDS)) }
                .httpClientCustomizer()
        )
    )
}

fun WebClient.ResponseSpec.exceptionHandler(): WebClient.ResponseSpec = onStatus(HttpStatusCode::isError) {
    throw UniplatException("error.api.internal")
}

inline fun <reified T> ConversionService.convert(source: Any): T {
    return convert(source, T::class.java)!!
}

suspend inline fun <reified T> PaginatedModel<*>.convertWith(conversionService: ConversionService): PaginatedResponse<T> {
    return PaginatedResponse(
        this.number,
        this.size,
        this.totalElements,
        this.totalPages,
        this.content.map { conversionService.convert<T>(it!!) }.toList()
    )
}

suspend fun <T, ID> CoroutineCrudRepository<T, ID>.saveUnique(
    entity: T,
    onDataIntegrityViolation: DataIntegrityViolationException.() -> Nothing
): T {
    return try {
        save(entity)
    } catch (e: DataIntegrityViolationException) {
        e.onDataIntegrityViolation()
    }
}

suspend fun <T> Validator.withValidateSuspend(any: Any, block: suspend () -> T): T {
    validateObject(any)
    return block()
}

private fun Validator.validateObject(any: Any) {
    val errorList = validate(any).map { violation ->
        com.uniplat.uniplatapi.exception.Error(
            violation.constraintDescriptor.annotation.annotationClass.simpleName ?: "",
            violation.messageTemplate
        )
    }

    if (validate(any).isNotEmpty()) throw BadRequestException("error.field.invalid", errors = errorList)
}

fun <T : Any> T.logger() = lazy { LoggerFactory.getLogger(javaClass) }

fun UserType.toAuthority(): List<GrantedAuthority> {
    return listOf(SimpleGrantedAuthority("ROLE_$this"))
}

fun Instant.toDate(): Date {
    return Date.from(this.atZone(ZoneId.systemDefault()).toInstant())
}

suspend fun <T : Mono<UserDetails>> T.generateAuthenticationToken(token: String): JwtAuthenticationToken {
    return JwtAuthenticationToken(token, this.awaitSingle(), this.awaitSingle().authorities.toList(), true)
}

@Suppress("UNCHECKED_CAST")
fun Claims.getRoles(): List<String> {
    return this["roles"]!! as List<String>
}

fun Claims.getUserId(): String {
    return this["userId"]!! as String
}

fun String.toUUID(): UUID {
    return UUID.fromString(this)
}

fun UserDetails.toUser(): User = this as User

private suspend fun getAuthentication() = ReactiveSecurityContextHolder.getContext().awaitSingle().authentication.principal as User

private suspend fun getAuthenticationOrNull() = ReactiveSecurityContextHolder.getContext().awaitSingleOrNull()?.authentication?.principal as? User

suspend fun <T> withAuthentication(block: suspend (User) -> T): T {
    return block(getAuthentication())
}

suspend fun <T> withAuthenticationOrNull(block: suspend (User?) -> T): T {
    return block(getAuthenticationOrNull())
}
