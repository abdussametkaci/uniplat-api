package com.uniplat.uniplatapi.domain.dto.response

import java.time.Instant
import java.util.UUID

data class UniversityResponse(
    val id: UUID,
    val name: String,
    val adminId: UUID,
    val version: Int,
    val createdAt: Instant,
    val lastModifiedAt: Instant
)
