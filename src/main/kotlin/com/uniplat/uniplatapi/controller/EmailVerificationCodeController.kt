package com.uniplat.uniplatapi.controller

import com.uniplat.uniplatapi.service.EmailVerificationCodeService
import com.uniplat.uniplatapi.utils.getURL
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/email")
class EmailVerificationCodeController(private val emailVerificationCodeService: EmailVerificationCodeService) {

    @GetMapping("/verify")
    suspend fun verifyUser(@RequestParam code: String) = emailVerificationCodeService.verifyUser(code)

    @PostMapping
    suspend fun sendEmailVerificationCodeByUserId(@RequestParam userId: UUID, serverHttpRequest: ServerHttpRequest) {
        emailVerificationCodeService.saveAndSendVerificationEmail(userId, getURL(serverHttpRequest))
    }
}
