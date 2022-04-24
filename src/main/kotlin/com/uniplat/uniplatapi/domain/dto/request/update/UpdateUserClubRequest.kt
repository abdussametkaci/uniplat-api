package com.uniplat.uniplatapi.domain.dto.request.update

import java.util.UUID

data class UpdateUserClubRequest(
    val clubId: UUID?,
    val userId: UUID?,
    val version: Int
)
