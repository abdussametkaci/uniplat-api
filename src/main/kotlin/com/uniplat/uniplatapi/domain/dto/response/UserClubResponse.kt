package com.uniplat.uniplatapi.domain.dto.response

import java.util.UUID

data class UserClubResponse(
    val id: UUID,
    val userId: UUID,
    val clubId: UUID
)
