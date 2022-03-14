package com.uniplat.uniplatapi.domain.dto.request

import com.uniplat.uniplatapi.domain.enums.Gender
import java.time.Instant
import java.util.UUID

data class UpdateUserRequest(
    val name: String?,
    val surname: String?,
    val gender: Gender?,
    val birthDate: Instant?,
    val password: String?,
    val universityId: UUID?,
    val description: String?,
    val profileImgId: UUID?,
    val messageAccessed: Boolean?,
    val version: Int
)
