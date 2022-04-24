package com.uniplat.uniplatapi.domain.dto.request.create

import java.util.UUID

data class CreateUserPostRequest(
    val userId: UUID,
    val postId: UUID
)
