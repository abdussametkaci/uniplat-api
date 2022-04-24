package com.uniplat.uniplatapi.domain.dto.request.create

import java.util.UUID

data class CreateUserClubRequest(
    val userId: UUID,
    val clubId: UUID
)
