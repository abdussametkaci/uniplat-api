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
import java.util.UUID

@Service
class UserLikedPostService(private val userLikedPostRepository: UserLikedPostRepository) {

    suspend fun getAll(userId: UUID?, postId: UUID?, pageable: Pageable): PaginatedModel<UserLikedPost> {
        val count = userLikedPostRepository.count(userId, postId)
        val userLikedPosts = userLikedPostRepository.findAllBy(userId, postId, pageable.offset, pageable.pageSize)

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

    suspend fun create(request: CreateUserLikedPostRequest) {
        with(request) {
            val userLikedPost = UserLikedPost(
                userId = userId,
                postId = postId
            )

            userLikedPostRepository.findByUserIdAndPostId(userId, postId)
                ?.let { delete(it.id!!) }
                ?: run {
                    userLikedPostRepository.saveUnique(userLikedPost) {
                        throw ConflictException(
                            "error.user-liked-post.conflict",
                            args = listOf(userId, postId)
                        )
                    }
                }
        }
    }

    suspend fun delete(id: UUID) {
        userLikedPostRepository.deleteById(id)
    }

    suspend fun deleteAllByUserId(userId: UUID) {
        userLikedPostRepository.deleteAllByUserId(userId)
    }
}
