package com.uniplat.uniplatapi.repository

import com.uniplat.uniplatapi.domain.enums.OwnerType
import com.uniplat.uniplatapi.domain.enums.PostType
import com.uniplat.uniplatapi.domain.model.Post
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface PostRepository : CoroutineCrudRepository<Post, UUID> {

    fun findAllBy(pageable: Pageable): Flow<Post>

    @Query(
        """
        SELECT count(*)
        FROM post
        WHERE (:ownerId IS NULL OR owner_id = :ownerId) AND (:ownerType IS NULL OR owner_type = :ownerType) AND (:postType IS NULL OR post_type = :postType)
        """
    )
    suspend fun count(ownerId: UUID?, ownerType: OwnerType?, postType: PostType?): Long

    @Query(
        """
        SELECT *
        FROM post
        WHERE (:ownerId IS NULL OR owner_id = :ownerId) AND (:ownerType IS NULL OR owner_type = :ownerType) AND (:postType IS NULL OR post_type = :postType)
        OFFSET :offset LIMIT :limit
        """
    )
    fun findAllBy(ownerId: UUID?, ownerType: OwnerType?, postType: PostType?, offset: Long, limit: Int): Flow<Post>

    @Query(
        """
        SELECT count(*)
        FROM post
        WHERE (owner_type, owner_id) IN (
            SELECT follow_type, follow_id
            FROM user_follow
            WHERE user_id = :userId
        )
        OR owner_id = :userId
        """
    )
    suspend fun countPostFlowByUserId(userId: UUID): Long

    @Query(
        """
        SELECT *
        FROM post
        WHERE (owner_type, owner_id) IN (
            SELECT follow_type, follow_id
            FROM user_follow
            WHERE user_id = :userId
        )
        OR owner_id = :userId
        ORDER BY created_at DESC
        OFFSET :offset LIMIT :limit
        """
    )
    fun postFlowByUserId(userId: UUID, offset: Long, limit: Int): Flow<Post>

    @Query(
        """
        SELECT *
        FROM post
        WHERE full_text @@ to_tsquery('simple', :text)
        OFFSET :offset LIMIT :limit
        """
    )
    fun findAllBy(text: String, offset: Long, limit: Int): Flow<Post>

    @Query(
        """
        SELECT count(*)
        FROM post
        WHERE full_text @@ to_tsquery('simple', :text)
        """
    )
    suspend fun count(text: String): Long
}
