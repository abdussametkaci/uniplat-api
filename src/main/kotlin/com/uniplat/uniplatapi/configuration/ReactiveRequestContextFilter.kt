package com.uniplat.uniplatapi.configuration

import com.uniplat.uniplatapi.configuration.holder.ReactiveRequestContextHolder
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Configuration
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
class ReactiveRequestContextFilter : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val request: ServerHttpRequest = exchange.request
        return chain.filter(exchange)
            .contextWrite { it.put(ReactiveRequestContextHolder.CONTEXT_KEY, request) }
    }
}
