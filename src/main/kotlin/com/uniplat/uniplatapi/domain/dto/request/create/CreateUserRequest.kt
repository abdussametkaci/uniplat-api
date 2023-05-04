package com.uniplat.uniplatapi.domain.dto.request.create

import com.uniplat.uniplatapi.domain.enums.Gender
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size
import java.time.Instant
import java.util.UUID

data class CreateUserRequest(
    @get:Size(min = 1, max = 255, message = "error.user.name-invalid")
    val name: String,

    @get:Size(min = 1, max = 255, message = "error.user.surname-invalid")
    val surname: String,

    val gender: Gender,
    val birthDate: Instant,

    @get:Email(message = "error.email.invalid")
    val email: String,

    @get:Size(min = 4, max = 255, message = "error.user.password-invalid")
    val password: String,

    val universityId: UUID?,

    @get:Size(min = 1, max = 255, message = "error.user.description-invalid")
    val description: String?,

    val profileImgId: UUID?,
    val messageAccessed: Boolean = true
)
