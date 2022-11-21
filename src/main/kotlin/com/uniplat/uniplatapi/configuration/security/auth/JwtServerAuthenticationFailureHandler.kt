package com.uniplat.uniplatapi.configuration.security.auth

import com.uniplat.uniplatapi.exception.UnauthorizedException
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class JwtServerAuthenticationFailureHandler : ServerAuthenticationFailureHandler {

    override fun onAuthenticationFailure(webFilterExchange: WebFilterExchange, exception: AuthenticationException): Mono<Void> = mono {
        val exchange = webFilterExchange.exchange ?: throw UnauthorizedException(AUTHENTICATION_REQUIRED)
        exchange.response.statusCode = HttpStatus.UNAUTHORIZED
        exchange.response.setComplete().awaitSingleOrNull()
    }

    private companion object {
        const val AUTHENTICATION_REQUIRED = "error.auth.required"
    }
}
