package com.uniplat.uniplatapi.domain.dto.response

import java.util.UUID

data class ActivityParticipantResponse(
    val id: UUID,
    val userId: UUID,
    val postId: UUID
)
