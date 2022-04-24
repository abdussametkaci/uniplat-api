package com.uniplat.uniplatapi.domain.dto.response

import java.time.Instant
import java.util.UUID

data class UserLikedPostResponse(
    val id: UUID,
    val userId: UUID,
    val postId: UUID,
    val createdAt: Instant
)
