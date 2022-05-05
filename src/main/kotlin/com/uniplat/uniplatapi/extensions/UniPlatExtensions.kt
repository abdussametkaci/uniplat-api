package com.uniplat.uniplatapi.extensions

import com.uniplat.uniplatapi.configuration.holder.ReactiveRequestContextHolder
import com.uniplat.uniplatapi.exception.BadRequestException
import com.uniplat.uniplatapi.exception.UniplatException
import com.uniplat.uniplatapi.model.PaginatedModel
import com.uniplat.uniplatapi.model.PaginatedResponse
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.core.convert.ConversionService
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.r2dbc.convert.EnumWriteSupport
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.http.HttpStatus
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.time.Duration
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.validation.Validator

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

fun WebClient.ResponseSpec.exceptionHandler(): WebClient.ResponseSpec = onStatus(HttpStatus::isError) {
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

suspend fun <T> withUserId(block: suspend (UUID) -> T): T {
    val userId =
        ReactiveRequestContextHolder.request
            .mapNotNull { request: ServerHttpRequest ->
                request.headers.getFirst("userId")
            }.awaitFirstOrNull()

    return if (userId != null) block(UUID.fromString(userId))
    else throw BadRequestException("error.*.userId-empty")
}
