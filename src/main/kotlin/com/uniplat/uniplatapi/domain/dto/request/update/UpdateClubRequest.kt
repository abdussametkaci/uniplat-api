package com.uniplat.uniplatapi.domain.dto.request.update

import jakarta.validation.constraints.Size
import java.util.UUID

data class UpdateClubRequest(
    @get:Size(min = 1, max = 255, message = "error.club.name-invalid")
    val name: String,

    @get:Size(min = 1, max = 255, message = "error.club.description-invalid")
    val description: String?,

    val profileImgId: UUID?,
    val adminId: UUID,
    val version: Int
)
