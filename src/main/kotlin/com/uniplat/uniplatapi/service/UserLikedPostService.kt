package com.uniplat.uniplatapi.service

import com.uniplat.uniplatapi.domain.dto.request.create.CreateUserLikedPostRequest
import com.uniplat.uniplatapi.domain.model.UserLikedPost
import com.uniplat.uniplatapi.exception.ConflictException
import com.uniplat.uniplatapi.exception.NotFoundException
import com.uniplat.uniplatapi.extensions.saveUnique
import com.uniplat.uniplatapi.model.PaginatedModel
import com.uniplat.uniplatapi.repository.UserLikedPostRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UserLikedPostService(
    private val userLikedPostRepository: UserLikedPostRepository,
    private val postService: PostService
) {

    suspend fun getAll(pageable: Pageable): PaginatedModel<UserLikedPost> {
        val count = userLikedPostRepository.count()
        val userLikedPosts = userLikedPostRepository.findAllBy(pageable)

        return PaginatedModel(
            content = userLikedPosts,
            number = pageable.pageNumber,
            size = pageable.pageSize,
            totalElements = count
        )
    }

    suspend fun getById(id: UUID): UserLikedPost {
        return userLikedPostRepository.findById(id) ?: throw NotFoundException("error.user-liked-post.not-found", args = listOf(id))
    }

    @Transactional
    suspend fun create(request: CreateUserLikedPostRequest): UserLikedPost {
        with(request) {
            val userLikedPost = UserLikedPost(
                userId = userId,
                postId = postId
            )

            postService.like(postId)

            return userLikedPostRepository.saveUnique(userLikedPost) {
                throw ConflictException(
                    "error.user-liked-post.conflict",
                    args = listOf(userId, postId)
                )
            }
        }
    }

    suspend fun delete(id: UUID) {
        userLikedPostRepository.deleteById(id)
    }
}
