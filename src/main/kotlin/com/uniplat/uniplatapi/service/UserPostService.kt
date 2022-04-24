package com.uniplat.uniplatapi.service

import com.uniplat.uniplatapi.domain.dto.request.create.CreateUserPostRequest
import com.uniplat.uniplatapi.domain.model.UserPost
import com.uniplat.uniplatapi.exception.ConflictException
import com.uniplat.uniplatapi.exception.NotFoundException
import com.uniplat.uniplatapi.extensions.saveUnique
import com.uniplat.uniplatapi.model.PaginatedModel
import com.uniplat.uniplatapi.repository.UserPostRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserPostService(private val userPostRepository: UserPostRepository) {

    suspend fun getAll(pageable: Pageable): PaginatedModel<UserPost> {
        val count = userPostRepository.count()
        val userPosts = userPostRepository.findAllBy(pageable)

        return PaginatedModel(
            content = userPosts,
            number = pageable.pageNumber,
            size = pageable.pageSize,
            totalElements = count
        )
    }

    suspend fun getById(id: UUID): UserPost {
        return userPostRepository.findById(id) ?: throw NotFoundException("error.user-post.not-found", args = listOf(id))
    }

    suspend fun create(request: CreateUserPostRequest): UserPost {
        with(request) {
            val userPost = UserPost(
                userId = userId,
                postId = postId
            )

            return userPostRepository.saveUnique(userPost) { throw ConflictException("error.user-post.conflict", args = listOf(userId, postId)) }
        }
    }

    suspend fun delete(id: UUID) {
        userPostRepository.deleteById(id)
    }
}
