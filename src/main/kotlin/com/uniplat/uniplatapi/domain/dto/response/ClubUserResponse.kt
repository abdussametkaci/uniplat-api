package com.uniplat.uniplatapi.domain.dto.response

import java.util.UUID

data class ClubUserResponse(
    val id: UUID,
    val clubId: UUID,
    val userId: UUID,
    val version: Int
)
