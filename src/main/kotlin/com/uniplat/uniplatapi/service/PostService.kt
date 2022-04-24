package com.uniplat.uniplatapi.service

import com.uniplat.uniplatapi.domain.dto.request.create.CreatePostRequest
import com.uniplat.uniplatapi.domain.dto.request.update.UpdatePostRequest
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

    suspend fun getAll(pageable: Pageable): PaginatedModel<Post> {
        val count = postRepository.count()
        val posts = postRepository.findAll(pageable)

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
        validate(request)
        with(request) {
            val post = Post(
                imgId = imgId,
                description = description,
                sharedPostId = sharedPostId
            )

            return postRepository.save(post)
        }
    }

    suspend fun update(id: UUID, request: UpdatePostRequest): Post {
        with(request) {
            val post = getById(id)

            description?.let { post.description = it }
            post.version = version

            return postRepository.save(post)
        }
    }

    suspend fun delete(id: UUID) {
        postRepository.deleteById(id)
    }

    suspend fun like(id: UUID) {
        val post = getById(id)
        post.likeCounter++
        postRepository.save(post)
    }

    private suspend fun validate(request: CreatePostRequest) {
        with(request) {
            if (imgId == null && description == null) throw BadRequestException("error.post.invalid")
        }
    }
}
