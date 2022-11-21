package com.uniplat.uniplatapi.configuration.security.auth

import com.uniplat.uniplatapi.exception.UnauthorizedException
import com.uniplat.uniplatapi.extensions.generateAuthenticationToken
import com.uniplat.uniplatapi.service.JwtService
import com.uniplat.uniplatapi.service.UserService
import kotlinx.coroutines.reactor.mono
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class JwtReactiveAuthenticationManager(
    private val userService: UserService,
    private val jwtService: JwtService
) : ReactiveAuthenticationManager {

    override fun authenticate(authentication: Authentication?): Mono<Authentication> = mono {
        authentication ?: return@mono null

        val jwtToken = authentication.credentials as String
        if (!jwtService.verify(jwtToken)) throw UnauthorizedException(INVALID_TOKEN)
        val claims = jwtService.getClaims(jwtToken)
        val email = claims.subject
        userService.findByUsername(email).generateAuthenticationToken(jwtToken)
    }

    private companion object {
        const val INVALID_TOKEN = "error.token.invalid"
    }
}
