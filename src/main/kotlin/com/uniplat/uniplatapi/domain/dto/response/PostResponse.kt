package com.uniplat.uniplatapi.domain.dto.response

import com.uniplat.uniplatapi.domain.enums.PostOwnerType
import com.uniplat.uniplatapi.domain.enums.PostType
import java.time.Instant
import java.util.UUID

data class PostResponse(
    val id: UUID,
    val imgId: UUID?,
    val description: String?,
    val postOwnerType: PostOwnerType,
    val postType: PostType,
    val ownerId: UUID,
    val sharedPostId: UUID?,
    val likeCounter: Int,
    val version: Int,
    val createdAt: Instant,
    val lastModifiedAt: Instant
)
