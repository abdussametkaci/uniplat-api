package com.uniplat.uniplatapi.domain.dto.request.create

import com.uniplat.uniplatapi.domain.enums.OwnerType
import java.util.UUID

data class CreateUserFollowRequest(
    val followType: OwnerType,
    val followId: UUID
)
