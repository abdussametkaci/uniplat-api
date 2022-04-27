package com.uniplat.uniplatapi.domain.dto.request.create

import java.util.UUID

data class CreateUserFollowRequest(
    val userId: UUID,
    val contactId: UUID
)
