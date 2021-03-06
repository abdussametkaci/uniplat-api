package com.uniplat.uniplatapi.domain.model

import com.uniplat.uniplatapi.domain.enums.Gender
import com.uniplat.uniplatapi.domain.enums.UserType
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.UUID

@Table("\"user\"")
data class User(
    @Id val id: UUID? = null,
    var name: String,
    var surname: String,
    var gender: Gender,
    var birthDate: Instant,
    val email: String,
    var password: String,
    var universityId: UUID? = null,
    val type: UserType,
    var description: String? = null,
    var profileImgId: UUID? = null,
    var messageAccessed: Boolean = true,
    var enabled: Boolean = false,
    @Version var version: Int? = null,
    @CreatedDate var createdAt: Instant? = null,
    @LastModifiedDate var lastModifiedAt: Instant? = null
)
