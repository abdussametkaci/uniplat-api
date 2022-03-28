package com.uniplat.uniplatapi.domain.dto.response

import java.util.UUID

data class UniversityClubResponse(
    val id: UUID,
    val universityId: UUID,
    val clubId: UUID,
    var version: Int
)
