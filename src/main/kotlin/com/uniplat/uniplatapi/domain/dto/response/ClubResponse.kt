package com.uniplat.uniplatapi.domain.dto.response

import java.util.UUID

data class ClubResponse(
    val id: UUID,
    val name: String,
    val universityId: UUID,
    val description: String?,
    val profileImgId: UUID?,
    val adminId: UUID,
    val version: Int
)
