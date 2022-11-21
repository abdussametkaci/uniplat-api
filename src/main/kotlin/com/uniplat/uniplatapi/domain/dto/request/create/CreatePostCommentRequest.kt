package com.uniplat.uniplatapi.domain.dto.request.create

import java.util.UUID
import javax.validation.constraints.Size

data class CreatePostCommentRequest(
    val postId: UUID,

    @get:Size(min = 1, max = 255, message = "error.post-comment.comment-invalid")
    val comment: String
)
