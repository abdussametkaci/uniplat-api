package com.uniplat.uniplatapi.domain.dto.response

import java.time.Instant
import java.util.UUID

data class UserPostResponse(
    val id: UUID,
    val userId: UUID,
    val postId: UUID,
    val version: Int?,
    val createdAt: Instant
)
