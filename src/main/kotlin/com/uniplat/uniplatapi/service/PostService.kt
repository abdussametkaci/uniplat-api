package com.uniplat.uniplatapi.service

import com.uniplat.uniplatapi.domain.dto.request.create.CreatePostRequest
import com.uniplat.uniplatapi.domain.dto.request.update.UpdatePostRequest
import com.uniplat.uniplatapi.domain.enums.OwnerType
import com.uniplat.uniplatapi.domain.enums.PostType
import com.uniplat.uniplatapi.domain.model.Post
import com.uniplat.uniplatapi.domain.model.PostDTO
import com.uniplat.uniplatapi.exception.BadRequestException
import com.uniplat.uniplatapi.exception.NotFoundException
import com.uniplat.uniplatapi.model.PaginatedModel
import com.uniplat.uniplatapi.repository.PostDTORepository
import com.uniplat.uniplatapi.repository.PostRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@Service
class PostService(
    private val postRepository: PostRepository,
    private val postDTORepository: PostDTORepository,
    private val postCommentService: PostCommentService,
    private val activityParticipantService: ActivityParticipantService,
    private val applicationScope: CoroutineScope
) {

    suspend fun getAll(ownerId: UUID?, ownerType: OwnerType?, postType: PostType?, pageable: Pageable): PaginatedModel<Post> {
        val count = postRepository.count(ownerId, ownerType, postType)
        val posts = postRepository.findAllBy(ownerId, ownerType, postType, pageable.offset, pageable.pageSize)

        return PaginatedModel(
            content = posts,
            number = pageable.pageNumber,
            size = pageable.pageSize,
            totalElements = count
        )
    }

    suspend fun getAll(userId: UUID, ownerId: UUID?, ownerType: OwnerType?, postType: PostType?, pageable: Pageable): PaginatedModel<PostDTO> {
        val count = postRepository.count(ownerId, ownerType, postType)
        val posts = postDTORepository.findAllBy(userId, ownerId, ownerType, postType, pageable.offset, pageable.pageSize)

        return PaginatedModel(
            content = posts,
            number = pageable.pageNumber,
            size = pageable.pageSize,
            totalElements = count
        )
    }

    suspend fun getById(id: UUID, userId: UUID): PostDTO {
        return postDTORepository.findById(id, userId) ?: throw NotFoundException("error.post.not-found", args = listOf(id))
    }

    suspend fun getById(id: UUID): Post {
        return postRepository.findById(id) ?: throw NotFoundException("error.post.not-found", args = listOf(id))
    }

    suspend fun create(request: CreatePostRequest): Post {
        validateCreate(request)
        with(request) {
            val post = Post(
                imgId = imgId,
                description = description,
                ownerType = ownerType,
                postType = postType,
                ownerId = ownerId,
                sharedPostId = sharedPostId,
                activityTitle = activityTitle,
                activityStartAt = activityStartAt,
                activityLocationDescription = activityLocationDescription,
                latitude = latitude,
                longitude = longitude
            )

            return postRepository.save(post)
        }
    }

    suspend fun update(id: UUID, request: UpdatePostRequest): Post {
        val post = getById(id)
        validateUpdate(request, post)
        with(request) {
            post.description = description
            post.activityStartAt = activityStartAt
            post.activityLocationDescription = activityLocationDescription
            post.latitude = latitude
            post.longitude = longitude
            post.version = version

            return postRepository.save(post)
        }
    }

    @Transactional
    suspend fun delete(id: UUID) {
        postRepository.deleteAndReturnById(id)?.let { post ->
            applicationScope.launch {
                launch { postCommentService.deleteAllByPostId(id) }
                if (post.postType == PostType.ACTIVITY) {
                    launch { activityParticipantService.deleteAllByPostId(id) }
                }
            }
        }
    }

    fun deleteAndReturnAllByOwnerIdAndOwnerType(ownerId: UUID, ownerType: OwnerType): Flow<Post> {
        return postRepository.deleteAndReturnAllByOwnerIdAndOwnerType(ownerId, ownerType)
    }

    suspend fun postFlowByUserId(userId: UUID, pageable: Pageable): PaginatedModel<PostDTO> {
        val count = postRepository.countPostFlowByUserId(userId)
        val posts = postDTORepository.postFlowByUserId(userId, pageable.offset, pageable.pageSize)

        return PaginatedModel(
            content = posts,
            number = pageable.pageNumber,
            size = pageable.pageSize,
            totalElements = count
        )
    }

    private suspend fun validateCreate(request: CreatePostRequest) {
        with(request) {
            if (postType == PostType.POST) {
                if (imgId == null && description == null) throw BadRequestException("error.post.invalid")
                sharedPostId?.let { postId ->
                    getById(postId).takeIf { it.sharedPostId != null }?.let { throw BadRequestException("error.post.shared-invalid") }
                }
                if (activityTitle != null || activityStartAt != null) throw BadRequestException("error.post.post-type-invalid")
            } else if (postType == PostType.ACTIVITY) {
                if (activityTitle == null || activityStartAt == null) throw BadRequestException("error.post.activity-type-invalid")
                if (activityStartAt.isBefore(Instant.now())) throw BadRequestException("error.post.activity-start-invalid")
            }
            validateLocation(latitude, longitude)
        }
    }

    private suspend fun validateUpdate(request: UpdatePostRequest, model: Post) {
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
            else {
                if (latitude < BigDecimal.valueOf(-90) || latitude > BigDecimal.valueOf(90)) throw BadRequestException(
                    "error.post.latitude-invalid",
                    args = listOf(latitude)
                )
                if (longitude < BigDecimal.valueOf(-180) || longitude > BigDecimal.valueOf(180)) throw BadRequestException(
                    "error.post.longitude-invalid",
                    args = listOf(longitude)
                )
            }
        }
    }
}
