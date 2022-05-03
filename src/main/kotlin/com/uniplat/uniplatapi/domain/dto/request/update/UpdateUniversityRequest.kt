package com.uniplat.uniplatapi.domain.dto.request.update

import java.util.UUID
import javax.validation.constraints.Size

data class UpdateUniversityRequest(
    @get:Size(min = 1, max = 255, message = "error.university.name-invalid")
    val name: String,

    @get:Size(min = 1, max = 255, message = "error.university.description-invalid")
    val description: String?,

    val profileImgId: UUID?,
    val adminId: UUID,
    val version: Int
)
