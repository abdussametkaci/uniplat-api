package com.uniplat.uniplatapi.domain.dto.request.create

import java.util.UUID

data class CreateUniversityUserRequest(
    val universityId: UUID,
    val userId: UUID,
)
