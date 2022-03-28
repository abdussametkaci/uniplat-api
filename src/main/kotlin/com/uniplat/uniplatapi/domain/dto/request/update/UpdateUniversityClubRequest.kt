package com.uniplat.uniplatapi.domain.dto.request.update

import java.util.UUID

data class UpdateUniversityClubRequest(
    val universityId: UUID?,
    val clubId: UUID?,
    val version: Int
)
