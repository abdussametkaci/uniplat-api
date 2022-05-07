package com.uniplat.uniplatapi.domain.dto.response

import java.time.Instant
import java.util.UUID

data class UniversityDTOResponse(
    val id: UUID,
    val name: String,
    val description: String?,
    val profileImgId: UUID?,
    val adminId: UUID,
    val version: Int,
    val createdAt: Instant,
    val followedByUser: Boolean,
    val countFollower: Long,
    val countClub: Long
)
