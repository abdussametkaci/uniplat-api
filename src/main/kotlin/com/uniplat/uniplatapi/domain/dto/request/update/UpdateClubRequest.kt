package com.uniplat.uniplatapi.domain.dto.request.update

import java.util.UUID
import javax.validation.constraints.Size

data class UpdateClubRequest(
    @get:Size(min = 1, max = 255, message = "error.club.name-invalid")
    val name: String?,

    val adminId: UUID?,
    val version: Int
)