package com.uniplat.uniplatapi.configuration.security.auth

import com.uniplat.uniplatapi.exception.UnauthorizedException
import kotlinx.coroutines.reactor.mono
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class JwtAuthenticationConverter : ServerAuthenticationConverter {

    override fun convert(exchange: ServerWebExchange): Mono<Authentication> = mono {
        val token = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)
            ?.also { if (!it.startsWith(TOKEN_PREFIX)) throw UnauthorizedException(INVALID_BEARER_TOKEN) }
            ?: return@mono null

        JwtAuthenticationToken(token.removePrefix(TOKEN_PREFIX))
    }

    private companion object {
        const val TOKEN_PREFIX = "Bearer "
        const val INVALID_BEARER_TOKEN = "error.auth.invalid-bearer-token"
    }
}
