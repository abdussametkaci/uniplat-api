package com.uniplat.uniplatapi.domain.dto.request.update

import java.util.UUID

data class UpdateUniversityUserRequest(
    val userId: UUID?,
    val universityId: UUID?,
    val version: Int
)
