package com.uniplat.uniplatapi.domain.dto.response

import com.uniplat.uniplatapi.domain.enums.OwnerType
import com.uniplat.uniplatapi.domain.enums.PostType
import java.time.Instant
import java.util.UUID

data class PostResponse(
    val id: UUID,
    val imgId: UUID?,
    val description: String?,
    val ownerType: OwnerType,
    val postType: PostType,
    val ownerId: UUID,
    val sharedPostId: UUID?,
    val activityStartAt: Instant?,
    val version: Int,
    val createdAt: Instant,
    val lastModifiedAt: Instant
)
