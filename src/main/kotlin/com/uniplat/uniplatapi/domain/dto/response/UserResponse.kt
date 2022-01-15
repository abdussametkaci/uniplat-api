package com.uniplat.uniplatapi.domain.dto.response

import com.uniplat.uniplatapi.domain.enums.Gender
import com.uniplat.uniplatapi.domain.enums.UserType
import java.time.Instant
import java.util.UUID

data class UserResponse(
    val id: UUID,
    val name: String,
    val surname: String,
    val gender: Gender,
    val birthDate: Instant,
    val email: String,
    val password: String,
    val universityId: UUID,
    val departmentId: UUID?,
    val type: UserType,
    val description: String?,
    val profileImgId: String?,
    val messageAccessed: Boolean,
    val version: Int,
    val createdAt: Instant,
    val lastModifiedAt: Instant
)
