package com.uniplat.uniplatapi.controller

import com.uniplat.uniplatapi.service.EmailVerificationCodeService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/verify")
class EmailVerificationCodeController(private val emailVerificationCodeService: EmailVerificationCodeService) {

    @GetMapping
    suspend fun verifyUser(@RequestParam code: String) = emailVerificationCodeService.verifyUser(code)
}
