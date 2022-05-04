package com.uniplat.uniplatapi.domain.model

import com.uniplat.uniplatapi.domain.enums.OwnerType
import com.uniplat.uniplatapi.domain.enums.PostType
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.UUID

@Table
data class Post(
    @Id val id: UUID? = null,
    val imgId: UUID? = null,
    var description: String? = null,
    val ownerType: OwnerType,
    val postType: PostType,
    val ownerId: UUID,
    var sharedPostId: UUID? = null,
    val activityTitle: String? = null,
    var activityStartAt: Instant? = null,
    @Version var version: Int? = null,
    @CreatedDate var createdAt: Instant? = null,
    @LastModifiedDate var lastModifiedAt: Instant? = null
)
