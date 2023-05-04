package com.uniplat.uniplatapi.domain.dto.request.create

import jakarta.validation.constraints.Size
import java.util.UUID

data class CreateClubRequest(
    @get:Size(min = 1, max = 255, message = "error.club.name-invalid")
    val name: String,

    val universityId: UUID,

    @get:Size(min = 1, max = 255, message = "error.club.description-invalid")
    val description: String?,

    val profileImgId: UUID?
)
