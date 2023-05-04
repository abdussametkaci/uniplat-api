package com.uniplat.uniplatapi.domain.dto.request.update

import jakarta.validation.constraints.Size

data class UpdateUserPasswordRequest(
    @get:Size(min = 4, max = 255, message = "error.user.password-invalid")
    val currentPassword: String,

    @get:Size(min = 4, max = 255, message = "error.user.password-invalid")
    val newPassword: String,

    val version: Int
)
