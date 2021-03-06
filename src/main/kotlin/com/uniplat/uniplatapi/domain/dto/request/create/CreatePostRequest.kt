package com.uniplat.uniplatapi.domain.dto.request.create

import com.uniplat.uniplatapi.domain.enums.OwnerType
import com.uniplat.uniplatapi.domain.enums.PostType
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID
import javax.validation.constraints.Size

data class CreatePostRequest(
    val imgId: UUID?,

    @get:Size(min = 1, max = 255, message = "error.post.description-invalid")
    val description: String?,

    val ownerType: OwnerType,
    val postType: PostType,
    val ownerId: UUID,
    val sharedPostId: UUID?,

    @get:Size(min = 1, max = 255, message = "error.post.activity-title-invalid")
    val activityTitle: String?,

    val activityStartAt: Instant?,

    @get:Size(min = 1, max = 255, message = "error.post.activity-location-description-invalid")
    val activityLocationDescription: String?,

    val latitude: BigDecimal?,
    val longitude: BigDecimal?
)
