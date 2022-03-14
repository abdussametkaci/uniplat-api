package com.uniplat.uniplatapi.domain.dto.request

import com.uniplat.uniplatapi.domain.enums.Gender
import java.time.Instant
import java.util.UUID
import javax.validation.constraints.Email

data class CreateUserRequest(
    val name: String,
    val surname: String,
    val gender: Gender,
    val birthDate: Instant,

    @get:Email(message = "error.email.invalid")
    val email: String,

    val password: String,
    val universityId: UUID,
    val description: String?,
    val profileImgId: UUID?,
    val messageAccessed: Boolean = true
)
