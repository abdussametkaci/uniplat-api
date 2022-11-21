package com.uniplat.uniplatapi.controller

import com.uniplat.uniplatapi.domain.dto.request.LoginRequest
import com.uniplat.uniplatapi.domain.dto.response.LoginResponse
import com.uniplat.uniplatapi.extensions.withAuthentication
import com.uniplat.uniplatapi.extensions.withValidateSuspend
import com.uniplat.uniplatapi.service.AuthenticationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Validator

@RestController
@RequestMapping("/auth")
class AuthenticationController(
    private val authenticationService: AuthenticationService,
    private val validator: Validator
) {

    @PostMapping("/login")
    suspend fun login(@RequestBody request: LoginRequest): LoginResponse {
        return validator.withValidateSuspend(request) {
            authenticationService.login(request)
        }
    }

    @PostMapping("/logout")
    suspend fun logout() = withAuthentication {
        authenticationService.logout(it.id!!)
    }
}
