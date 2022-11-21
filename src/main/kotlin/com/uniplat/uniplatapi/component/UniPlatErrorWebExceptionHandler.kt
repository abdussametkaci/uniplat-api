package com.uniplat.uniplatapi.component

import com.fasterxml.jackson.databind.ObjectMapper
import com.uniplat.uniplatapi.exception.BaseResponseStatusException
import com.uniplat.uniplatapi.exception.ErrorResponse
import com.uniplat.uniplatapi.exception.SubError
import com.uniplat.uniplatapi.extensions.logger
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
@Order(-2)
class UniPlatErrorWebExceptionHandler(
    private val objectMapper: ObjectMapper,
    private val messageSource: MessageSource
) : ErrorWebExceptionHandler {

    private val logger by logger()

    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        val dataBufferFactory = exchange.response.bufferFactory()

        val status: HttpStatus
        val errorResponse: ErrorResponse

        when {
            ex is BaseResponseStatusException -> {
                status = ex.status
                errorResponse = ErrorResponse(
                    ex.code,
                    getLocalizedMessage(ex),
                    ex.errors.map { SubError(it.code, getLocalizedMessage(it)) }
                )
            }
            ex is ResponseStatusException -> {
                status = ex.status
                errorResponse = ErrorResponse(
                    ex.status.name,
                    ex.reason ?: ex.message,
                    emptyList()
                )
            }
            ex.javaClass.name == OPTIMISTIC_LOCKING_FAILURE_EXCEPTION -> {
                status = HttpStatus.CONFLICT
                errorResponse = ErrorResponse(
                    "OPTIMISTIC_LOCK",
                    getLocalizedMessage(com.uniplat.uniplatapi.exception.Error(code = "OPTIMISTIC_LOCK", messageProperty = "error.*.optimistic-lock")),
                    emptyList()
                )
            }
            else -> {
                status = HttpStatus.INTERNAL_SERVER_ERROR
                errorResponse = ErrorResponse(
                    "INTERNAL_SERVER_ERROR",
                    ex.message ?: "",
                    emptyList()
                )
            }
        }

        logger.error("An exception occurred", ex)

        val dataBuffer = Mono.fromCallable { dataBufferFactory.wrap(objectMapper.writeValueAsBytes(errorResponse)) }
        exchange.response.apply {
            statusCode = status
            headers.contentType = MediaType.APPLICATION_JSON
        }

        return exchange.response.writeWith(dataBuffer)
    }

    private fun getLocalizedMessage(ex: BaseResponseStatusException): String {
        ex.messageProperty?.let {
            return messageSource.getMessage(ex.messageProperty!!, ex.messageArgs.map { it.toString() }.toTypedArray(), LocaleContextHolder.getLocale())
        }
        return ex.reason ?: ex.message
    }

    private fun getLocalizedMessage(error: com.uniplat.uniplatapi.exception.Error): String {
        error.messageProperty?.let {
            return messageSource.getMessage(error.messageProperty, error.messageArgs.map { it.toString() }.toTypedArray(), LocaleContextHolder.getLocale())
        }
        return error.message ?: ""
    }

    private companion object {
        const val OPTIMISTIC_LOCKING_FAILURE_EXCEPTION = "org.springframework.dao.OptimisticLockingFailureException"
    }
}
