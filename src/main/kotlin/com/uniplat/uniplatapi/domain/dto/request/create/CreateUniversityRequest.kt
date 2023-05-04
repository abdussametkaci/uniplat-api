package com.uniplat.uniplatapi.domain.dto.request.create

import jakarta.validation.constraints.Size
import java.util.UUID

data class CreateUniversityRequest(
    @get:Size(min = 1, max = 255, message = "error.university.name-invalid")
    val name: String,

    @get:Size(min = 1, max = 255, message = "error.university.description-invalid")
    val description: String?,

    val profileImgId: UUID?
)
