package com.uniplat.uniplatapi.controller

import com.uniplat.uniplatapi.component.ContextAwareConversionService
import com.uniplat.uniplatapi.component.convert
import com.uniplat.uniplatapi.domain.dto.response.UserResponse
import com.uniplat.uniplatapi.service.LoginService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/login")
class LoginController(
    private val loginService: LoginService,
    private val conversionService: ContextAwareConversionService
) {

    @PostMapping
    suspend fun login(@RequestParam email: String, @RequestParam password: String): UserResponse {
        return conversionService.convert(loginService.login(email, password))
    }
}
