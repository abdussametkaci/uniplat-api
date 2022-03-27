package com.uniplat.uniplatapi.domain.dto.request.create

import java.util.UUID

data class CreateClubUserRequest(
    val clubId: UUID,
    val userId: UUID
)
