package com.uniplat.uniplatapi.domain.dto.request.update

import java.util.UUID

data class UpdateClubUserRequest(
    val clubId: UUID?,
    val userId: UUID?,
    val version: Int
)
