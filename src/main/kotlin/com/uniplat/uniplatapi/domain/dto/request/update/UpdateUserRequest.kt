package com.uniplat.uniplatapi.domain.dto.request.update

import com.uniplat.uniplatapi.domain.enums.Gender
import jakarta.validation.constraints.Size
import java.time.Instant
import java.util.UUID

data class UpdateUserRequest(
    @get:Size(min = 1, max = 255, message = "error.user.name-invalid")
    val name: String,

    @get:Size(min = 1, max = 255, message = "error.user.surname-invalid")
    val surname: String,

    val gender: Gender,
    val birthDate: Instant,
    val universityId: UUID?,

    @get:Size(min = 1, max = 255, message = "error.user.description-invalid")
    val description: String?,

    val profileImgId: UUID?,
    val messageAccessed: Boolean,
    val version: Int
)
