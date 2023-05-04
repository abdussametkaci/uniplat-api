package com.uniplat.uniplatapi.domain.dto.request.create

import jakarta.validation.constraints.Size
import java.util.UUID

data class CreatePostCommentRequest(
    val postId: UUID,

    @get:Size(min = 1, max = 255, message = "error.post-comment.comment-invalid")
    val comment: String
)
