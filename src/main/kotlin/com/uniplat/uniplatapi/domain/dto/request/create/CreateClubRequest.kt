package com.uniplat.uniplatapi.domain.dto.request.create

import java.util.UUID
import javax.validation.constraints.Size

data class CreateClubRequest(
    @get:Size(min = 1, max = 255, message = "error.club.name-invalid")
    val name: String,

    val universityId: UUID,
    val adminId: UUID
)
