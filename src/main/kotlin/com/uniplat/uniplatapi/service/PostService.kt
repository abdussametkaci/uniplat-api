package com.uniplat.uniplatapi.service

import com.uniplat.uniplatapi.domain.dto.request.create.CreatePostRequest
import com.uniplat.uniplatapi.domain.dto.request.update.UpdatePostRequest
import com.uniplat.uniplatapi.domain.enums.OwnerType
import com.uniplat.uniplatapi.domain.model.Post
import com.uniplat.uniplatapi.exception.BadRequestException
import com.uniplat.uniplatapi.exception.NotFoundException
import com.uniplat.uniplatapi.model.PaginatedModel
import com.uniplat.uniplatapi.repository.PostRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PostService(private val postRepository: PostRepository) {

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
                sharedPostId = sharedPostId
            )

            return postRepository.save(post)
        }
    }

    suspend fun update(id: UUID, request: UpdatePostRequest): Post {
        val post = getById(id)
        validateUpdate(request, post)
        with(request) {
            post.description = description
            post.version = version

            return postRepository.save(post)
        }
    }

    suspend fun delete(id: UUID) {
        postRepository.deleteById(id)
    }

    suspend fun postFlowByUserId(userId: UUID, pageable: Pageable): PaginatedModel<Post> {
        val count = postRepository.countPostFlowByUserId(userId)
        val posts = postRepository.postFlowByUserId(userId, pageable.offset, pageable.pageSize)

        return PaginatedModel(
            content = posts,
            number = pageable.pageNumber,
            size = pageable.pageSize,
            totalElements = count
        )
    }

    private suspend fun validateCreate(request: CreatePostRequest) {
        with(request) {
            if (imgId == null && description == null) throw BadRequestException("error.post.invalid")
        }
    }

    private suspend fun validateUpdate(request: UpdatePostRequest, model: Post) {
        with(request) {
            if (description == null && model.imgId == null) throw BadRequestException("error.post.update-invalid")
        }
    }
}
