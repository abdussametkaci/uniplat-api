package com.uniplat.uniplatapi.repository

import com.uniplat.uniplatapi.domain.model.UserFollow
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface UserFollowRepository : CoroutineCrudRepository<UserFollow, UUID> {

    fun findAllBy(pageable: Pageable): Flow<UserFollow>

    @Query(
        """
        SELECT count(*)
        FROM user_follow
        WHERE (:userId IS NULL OR user_id = :userId) AND (:followerId IS NULL OR follower_id = :followerId)
        """
    )
    suspend fun count(userId: UUID?, followerId: UUID?): Long

    @Query(
        """
        SELECT *
        FROM user_follow
        WHERE (:userId IS NULL OR user_id = :userId) AND (:followerId IS NULL OR follower_id = :followerId)
        OFFSET :offset LIMIT :limit
        """
    )
    fun findAllBy(userId: UUID?, followerId: UUID?, offset: Long, limit: Int): Flow<UserFollow>
}
