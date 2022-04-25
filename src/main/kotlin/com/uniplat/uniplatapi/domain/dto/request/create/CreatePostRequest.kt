package com.uniplat.uniplatapi.domain.dto.request.create

import com.uniplat.uniplatapi.domain.enums.PostOwnerType
import com.uniplat.uniplatapi.domain.enums.PostType
import java.util.UUID
import javax.validation.constraints.Size

data class CreatePostRequest(
    val imgId: UUID?,

    @get:Size(min = 1, max = 255, message = "error.post.description-invalid")
    val description: String?,

    val postOwnerType: PostOwnerType,
    val postType: PostType,
    val ownerId: UUID,
    val sharedPostId: UUID?
)
