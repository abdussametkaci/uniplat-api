package com.uniplat.uniplatapi.repository

import com.uniplat.uniplatapi.domain.model.PostComment
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface PostCommentRepository : CoroutineCrudRepository<PostComment, UUID> {

    fun findAllBy(pageable: Pageable): Flow<PostComment>

    @Query(
        """
        SELECT count(*)
        FROM post_comment
        WHERE (:userId IS NULL OR user_id = :userId) AND (:postId IS NULL OR post_id = :postId)
        """
    )
    suspend fun count(userId: UUID?, postId: UUID?): Long

    @Query(
        """
        SELECT *
        FROM post_comment
        WHERE (:userId IS NULL OR user_id = :userId) AND (:postId IS NULL OR post_id = :postId)
        OFFSET :offset LIMIT :limit
        """
    )
    fun findAllBy(userId: UUID?, postId: UUID?, offset: Long, limit: Int): Flow<PostComment>
}
