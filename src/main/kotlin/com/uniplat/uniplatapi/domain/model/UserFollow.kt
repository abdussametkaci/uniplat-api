package com.uniplat.uniplatapi.domain.model

import com.uniplat.uniplatapi.domain.enums.OwnerType
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.UUID

@Table
data class UserFollow(
    @Id val id: UUID? = null,
    val userId: UUID,
    val followType: OwnerType,
    val followId: UUID,
    @Version var version: Int? = null,
    @CreatedDate var createdAt: Instant? = null,
    @LastModifiedDate var lastModifiedAt: Instant? = null
)
