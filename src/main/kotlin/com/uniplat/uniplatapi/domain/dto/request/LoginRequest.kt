package com.uniplat.uniplatapi.domain.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

data class LoginRequest(
    @get:Email(message = "error.email.invalid")
    val email: String,

    @get:Size(min = 4, max = 255, message = "error.user.password-invalid")
    val password: String
)
