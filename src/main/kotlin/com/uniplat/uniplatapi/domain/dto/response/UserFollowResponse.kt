package com.uniplat.uniplatapi.domain.dto.response

import java.util.UUID

data class UserFollowResponse(
    val userId: UUID,
    val followerId: UUID
)
