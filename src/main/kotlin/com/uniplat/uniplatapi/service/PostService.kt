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
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class PostService(
    private val postRepository: PostRepository,
    private val postDTORepository: PostDTORepository
) {

    suspend fun getAll(ownerId: UUID?, ownerType: OwnerType?, pageable: Pageable): PaginatedModel<Post> {
        val count = postRepository.count(ownerId, ownerType)
        val posts = postRepository.findAllBy(ownerId, ownerType, pageable.offset, pageable.pageSize)

        return PaginatedModel(
            content = posts,
            number = pageable.pageNumber,
            size = pageable.pageSize,
            totalElements = count
        )
    }

    suspend fun getAll(userId: UUID, ownerId: UUID?, ownerType: OwnerType?, pageable: Pageable): PaginatedModel<PostDTO> {
        val count = postRepository.count(ownerId, ownerType)
        val posts = postDTORepository.findAllBy(userId, ownerId, ownerType, pageable.offset, pageable.pageSize)

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
                activityStartAt = activityStartAt
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
            post.version = version

            return postRepository.save(post)
        }
    }

    suspend fun delete(id: UUID) {
        postRepository.deleteById(id)
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
        }
    }
}
