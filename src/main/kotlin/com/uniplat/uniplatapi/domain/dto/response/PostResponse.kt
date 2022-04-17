package com.uniplat.uniplatapi.domain.dto.response

import java.time.Instant
import java.util.UUID

data class PostResponse(
    val id: UUID,
    val imgId: UUID,
    val description: String?,
    val likeCounter: Int,
    val sharedPostId: UUID?,
    val version: Int,
    val createdAt: Instant,
    val lastModifiedAt: Instant
)
