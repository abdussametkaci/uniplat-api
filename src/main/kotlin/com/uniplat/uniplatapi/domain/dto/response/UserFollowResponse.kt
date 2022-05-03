package com.uniplat.uniplatapi.domain.dto.response

import com.uniplat.uniplatapi.domain.enums.OwnerType
import java.util.UUID

data class UserFollowResponse(
    val id: UUID,
    val userId: UUID,
    val followType: OwnerType,
    val followId: UUID
)
