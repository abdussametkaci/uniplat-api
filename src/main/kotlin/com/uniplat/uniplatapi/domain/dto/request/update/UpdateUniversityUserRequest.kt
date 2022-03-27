package com.uniplat.uniplatapi.domain.dto.request.update

import java.util.UUID

data class UpdateUniversityUserRequest(
    val universityId: UUID?,
    val userId: UUID?,
    val version: Int
)
