package com.uniplat.uniplatapi.service

import com.uniplat.uniplatapi.domain.dto.request.create.CreatePostCommentRequest
import com.uniplat.uniplatapi.domain.model.PostComment
import com.uniplat.uniplatapi.model.PaginatedModel
import com.uniplat.uniplatapi.repository.PostCommentRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PostCommentService(private val postCommentRepository: PostCommentRepository) {

    suspend fun getAll(userId: UUID?, postId: UUID?, pageable: Pageable): PaginatedModel<PostComment> {
        val count = postCommentRepository.count(userId, postId)
        val postComments = postCommentRepository.findAllBy(userId, postId, pageable.offset, pageable.pageSize)

        return PaginatedModel(
            content = postComments,
            number = pageable.pageNumber,
            size = pageable.pageSize,
            totalElements = count
        )
    }

    suspend fun create(request: CreatePostCommentRequest): PostComment {
        with(request) {
            val userClub = PostComment(
                userId = userId,
                postId = postId,
                comment = comment
            )

            return postCommentRepository.save(userClub)
        }
    }

    suspend fun delete(id: UUID) {
        postCommentRepository.deleteById(id)
    }
}
