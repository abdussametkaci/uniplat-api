package com.uniplat.uniplatapi.domain.dto.request.create

import java.util.UUID

data class CreateUserLikedPostRequest(
    val userId: UUID,
    val postId: UUID
)
