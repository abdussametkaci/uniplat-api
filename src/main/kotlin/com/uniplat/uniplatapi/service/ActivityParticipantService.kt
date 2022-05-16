package com.uniplat.uniplatapi.service

import com.uniplat.uniplatapi.domain.dto.request.create.CreateActivityParticipantRequest
import com.uniplat.uniplatapi.domain.enums.PostType
import com.uniplat.uniplatapi.domain.model.ActivityParticipant
import com.uniplat.uniplatapi.exception.BadRequestException
import com.uniplat.uniplatapi.exception.ConflictException
import com.uniplat.uniplatapi.extensions.saveUnique
import com.uniplat.uniplatapi.model.PaginatedModel
import com.uniplat.uniplatapi.repository.ActivityParticipantRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class ActivityParticipantService(
    private val activityParticipantRepository: ActivityParticipantRepository,
    private val postService: PostService
) {

    suspend fun getAll(userId: UUID?, postId: UUID?, pageable: Pageable): PaginatedModel<ActivityParticipant> {
        val count = activityParticipantRepository.count(userId, postId)
        val activityParticipants = activityParticipantRepository.findAllBy(userId, postId, pageable.offset, pageable.pageSize)

        return PaginatedModel(
            content = activityParticipants,
            number = pageable.pageNumber,
            size = pageable.pageSize,
            totalElements = count
        )
    }

    suspend fun create(request: CreateActivityParticipantRequest): ActivityParticipant {
        with(request) {
            validate(postId)

            val userClub = ActivityParticipant(
                userId = userId,
                postId = postId
            )

            return activityParticipantRepository.saveUnique(userClub) {
                throw ConflictException(
                    "error.activity-participant.conflict",
                    args = listOf(userId, postId)
                )
            }
        }
    }

    suspend fun delete(id: UUID) {
        activityParticipantRepository.deleteById(id)
    }

    suspend fun deleteAllByUserId(userId: UUID) {
        activityParticipantRepository.deleteAllByUserId(userId)
    }

    suspend fun deleteAllByPostId(postId: UUID) {
        activityParticipantRepository.deleteAllByPostId(postId)
    }

    private suspend fun validate(postId: UUID) {
        val post = postService.getById(postId)
        if (post.postType != PostType.ACTIVITY) {
            throw BadRequestException("error.activity-participant.invalid")
        } else {
            if (post.activityStartAt!!.isBefore(Instant.now())) throw BadRequestException("error.activity-participant.passed-invalid")
        }
    }
}
