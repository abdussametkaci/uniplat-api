package com.uniplat.uniplatapi.service

import com.uniplat.uniplatapi.domain.dto.request.create.CreatePostRequest
import com.uniplat.uniplatapi.domain.dto.request.update.UpdatePostRequest
import com.uniplat.uniplatapi.domain.enums.OwnerType
import com.uniplat.uniplatapi.domain.enums.PostType
import com.uniplat.uniplatapi.domain.model.Post
import com.uniplat.uniplatapi.domain.model.PostDTO
import com.uniplat.uniplatapi.exception.NotFoundException
import com.uniplat.uniplatapi.model.PaginatedModel
import com.uniplat.uniplatapi.repository.PostDTORepository
import com.uniplat.uniplatapi.repository.PostRepository
import com.uniplat.uniplatapi.validation.PostValidator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class PostService(
    private val postRepository: PostRepository,
    private val postDTORepository: PostDTORepository,
    private val postCommentService: PostCommentService,
    private val activityParticipantService: ActivityParticipantService,
    private val postValidator: PostValidator,
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
        postValidator.validate(request)
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
        postValidator.validate(request, post)
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
}
