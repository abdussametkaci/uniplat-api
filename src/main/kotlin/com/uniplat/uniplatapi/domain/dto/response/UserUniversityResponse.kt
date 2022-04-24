package com.uniplat.uniplatapi.domain.dto.response

import java.util.UUID

data class UserUniversityResponse(
    val id: UUID,
    val userId: UUID,
    val universityId: UUID
)
