package com.uniplat.uniplatapi.domain.dto.response

import java.time.Instant
import java.util.UUID

data class ClubDTOResponse(
    val id: UUID,
    val name: String,
    val universityId: UUID,
    val description: String?,
    val profileImgId: UUID?,
    val adminId: UUID,
    val version: Int,
    val createdAt: Instant,
    val followedByUser: Boolean,
    val countFollower: Long
)
