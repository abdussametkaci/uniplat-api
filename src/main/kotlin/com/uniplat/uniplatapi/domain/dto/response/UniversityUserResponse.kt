package com.uniplat.uniplatapi.domain.dto.response

import java.util.UUID

data class UniversityUserResponse(
    val id: UUID,
    val universityId: UUID,
    val userId: UUID,
    val version: Int
)
