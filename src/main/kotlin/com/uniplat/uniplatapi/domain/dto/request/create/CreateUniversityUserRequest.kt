package com.uniplat.uniplatapi.domain.dto.request.create

import java.util.UUID

data class CreateUniversityUserRequest(
    val userId: UUID,
    val universityId: UUID
)
