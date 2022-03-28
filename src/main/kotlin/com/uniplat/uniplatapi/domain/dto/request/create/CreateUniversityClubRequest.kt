package com.uniplat.uniplatapi.domain.dto.request.create

import java.util.UUID

data class CreateUniversityClubRequest(
    val universityId: UUID,
    val clubId: UUID
)
