package com.uniplat.uniplatapi.domain.dto.request

import com.uniplat.uniplatapi.domain.enums.Gender
import java.time.Instant
import java.util.UUID

data class CreateUserRequest(
    val name: String,
    val surname: String,
    val gender: Gender,
    val birthDate: Instant,
    val email: String,
    val password: String,
    val universityId: UUID,
    val description: String?,
    val profileImgId: String?
)
