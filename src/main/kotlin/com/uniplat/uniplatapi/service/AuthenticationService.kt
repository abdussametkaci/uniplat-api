package com.uniplat.uniplatapi.service

import com.uniplat.uniplatapi.domain.dto.request.LoginRequest
import com.uniplat.uniplatapi.domain.dto.response.LoginResponse
import com.uniplat.uniplatapi.domain.model.Session
import com.uniplat.uniplatapi.exception.BadRequestException
import com.uniplat.uniplatapi.extensions.toUUID
import com.uniplat.uniplatapi.extensions.toUser
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AuthenticationService(
    private val userService: UserService,
    private val jwtService: JwtService,
    private val sessionService: SessionService,
    private val passwordEncoder: PasswordEncoder
) {

    suspend fun login(request: LoginRequest): LoginResponse {
        with(request) {
            val user = userService.findByUsername(email).awaitSingle().toUser()
                .also { if (!passwordEncoder.matches(password, it.password)) throw BadRequestException("error.login.email-verification-invalid") }

            val token = jwtService.generateToken(user.id.toString(), email, listOf(user.type))
            val jti = jwtService.getClaims(token).id

            sessionService.findByUserId(user.id!!)?.let {
                sessionService.save(it.copy(jti = jti.toUUID()))
            } ?: sessionService.save(Session(userId = user.id, jti = jti.toUUID()))

            return LoginResponse(token)
        }
    }

    suspend fun logout(userId: UUID) {
        sessionService.deleteByUserId(userId)
    }
}
