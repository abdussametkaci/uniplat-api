package com.uniplat.uniplatapi.domain.dto.response

import com.uniplat.uniplatapi.domain.enums.Gender
import com.uniplat.uniplatapi.domain.enums.UserType
import java.time.Instant
import java.util.UUID

data class UserDTOResponse(
    val id: UUID,
    val name: String,
    val surname: String,
    val gender: Gender,
    val birthDate: Instant,
    val email: String,
    val universityId: UUID?,
    val type: UserType,
    val description: String?,
    val profileImgId: UUID?,
    val messageAccessed: Boolean,
    val version: Int,
    val followedByUser: Boolean,
    val countFollower: Long,
    val countFollow: Long
)
