package com.uniplat.uniplatapi.validation

import com.uniplat.uniplatapi.domain.dto.request.create.CreatePostRequest
import com.uniplat.uniplatapi.domain.dto.request.update.UpdatePostRequest
import com.uniplat.uniplatapi.domain.enums.PostType
import com.uniplat.uniplatapi.domain.model.Post
import com.uniplat.uniplatapi.exception.BadRequestException
import com.uniplat.uniplatapi.service.PostService
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.Instant

@Component
class PostValidator(@Lazy private val postService: PostService) {

    suspend fun validate(request: CreatePostRequest) {
        with(request) {
            if (postType == PostType.POST) {
                if (imgId == null && description == null) throw BadRequestException("error.post.invalid")
                sharedPostId?.let { postId ->
                    postService.getById(postId).takeIf { it.sharedPostId != null }?.let { throw BadRequestException("error.post.shared-invalid") }
                }
                if (activityTitle != null || activityStartAt != null) throw BadRequestException("error.post.post-type-invalid")
            } else if (postType == PostType.ACTIVITY) {
                if (activityTitle == null || activityStartAt == null) throw BadRequestException("error.post.activity-type-invalid")
                if (activityStartAt.isBefore(Instant.now())) throw BadRequestException("error.post.activity-start-invalid")
            }

            validateLocation(latitude, longitude)
        }
    }

    suspend fun validate(request: UpdatePostRequest, model: Post) {
        with(request) {
            if (model.postType == PostType.POST) {
                if (description == null && model.imgId == null) throw BadRequestException("error.post.update-invalid")
            } else if (model.postType == PostType.ACTIVITY) {
                if (Instant.now().isAfter(model.activityStartAt!!)) throw BadRequestException("error.post.update-activity-started-invalid")
                if (activityStartAt!!.isBefore(Instant.now())) throw BadRequestException("error.post.update-activity-start-invalid")
            }

            validateLocation(latitude, longitude)
        }
    }

    private suspend fun validateLocation(latitude: BigDecimal?, longitude: BigDecimal?) {
        if (latitude != null || longitude != null) {
            if (latitude == null || longitude == null) throw BadRequestException("error.post.location-invalid")
        }
    }
}
