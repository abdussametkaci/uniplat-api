package com.uniplat.uniplatapi.domain.dto.request.create

import java.util.UUID
import javax.validation.constraints.Size

data class CreateUniversityRequest(
    @get:Size(min = 1, max = 255, message = "error.university.name-invalid")
    val name: String,

    val adminId: UUID,
)
