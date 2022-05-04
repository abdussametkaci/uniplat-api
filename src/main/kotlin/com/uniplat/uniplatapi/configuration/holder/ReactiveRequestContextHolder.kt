package com.uniplat.uniplatapi.configuration.holder

import org.springframework.http.server.reactive.ServerHttpRequest
import reactor.core.publisher.Mono

object ReactiveRequestContextHolder {
    val CONTEXT_KEY: Class<ServerHttpRequest> = ServerHttpRequest::class.java
    val request: Mono<ServerHttpRequest>
        get() = Mono.subscriberContext()
            .map { ctx -> ctx.get(CONTEXT_KEY) }
}
