package com.uniplat.uniplatapi.domain.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.UUID

@Table
data class ClubUser(
    @Id val id: UUID? = null,
    var clubId: UUID,
    var userId: UUID,
    @Version var version: Int? = null,
    @CreatedDate var createdAt: Instant? = null,
    @LastModifiedDate var lastModifiedAt: Instant? = null
)
