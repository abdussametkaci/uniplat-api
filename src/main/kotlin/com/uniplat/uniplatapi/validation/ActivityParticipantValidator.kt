package com.uniplat.uniplatapi.validation

import com.uniplat.uniplatapi.domain.enums.PostType
import com.uniplat.uniplatapi.exception.BadRequestException
import com.uniplat.uniplatapi.service.PostService
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.UUID

@Component
class ActivityParticipantValidator(@Lazy private val postService: PostService) {

    suspend fun validate(postId: UUID) {
        val post = postService.getById(postId)
        if (post.postType != PostType.ACTIVITY) {
            throw BadRequestException("error.activity-participant.invalid")
        } else {
            if (post.activityStartAt!!.isBefore(Instant.now())) throw BadRequestException("error.activity-participant.passed-invalid")
        }
    }
}
