package com.uniplat.uniplatapi.domain.model

import java.time.Instant
import java.util.UUID

data class ClubDTO(
    val id: UUID,
    val name: String,
    val universityId: UUID,
    val description: String?,
    val profileImgId: UUID?,
    val adminId: UUID,
    val version: Int,
    val createdAt: Instant,
    val lastModifiedAt: Instant,
    val followedByUser: Boolean,
    val countFollower: Long
)
