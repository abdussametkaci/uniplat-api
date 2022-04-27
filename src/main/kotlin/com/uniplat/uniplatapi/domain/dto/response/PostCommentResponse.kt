package com.uniplat.uniplatapi.domain.dto.response

import java.time.Instant
import java.util.UUID

data class PostCommentResponse(
    val userId: UUID,
    val postId: UUID,
    val comment: String,
    val createdAt: Instant
)
