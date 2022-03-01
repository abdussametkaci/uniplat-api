package com.uniplat.uniplatapi.extensions

import com.uniplat.uniplatapi.exception.UniplatException
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import org.springframework.data.r2dbc.convert.EnumWriteSupport
import org.springframework.http.HttpStatus
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.time.Duration
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

fun WebClient.ResponseSpec.exceptionHandler(): WebClient.ResponseSpec = onStatus(HttpStatus::isError) {
    throw UniplatException("error.api.internal")
}
