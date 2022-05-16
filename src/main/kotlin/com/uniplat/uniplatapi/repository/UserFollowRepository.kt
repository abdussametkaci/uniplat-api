package com.uniplat.uniplatapi.repository

import com.uniplat.uniplatapi.domain.enums.OwnerType
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
        WHERE (:userId IS NULL OR user_id = :userId) AND (:followType IS NULL OR follow_type = :followType) AND (:followId IS NULL OR follow_id = :followId)
        """
    )
    suspend fun count(userId: UUID?, followType: OwnerType?, followId: UUID?): Long

    @Query(
        """
        SELECT *
        FROM user_follow
        WHERE (:userId IS NULL OR user_id = :userId) AND (:followType IS NULL OR follow_type = :followType) AND (:followId IS NULL OR follow_id = :followId)
        OFFSET :offset LIMIT :limit
        """
    )
    fun findAllBy(userId: UUID?, followType: OwnerType?, followId: UUID?, offset: Long, limit: Int): Flow<UserFollow>

    suspend fun deleteAllByUserId(userId: UUID)
}
