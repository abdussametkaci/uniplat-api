package com.uniplat.uniplatapi.repository

import com.uniplat.uniplatapi.domain.model.ActivityParticipant
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface ActivityParticipantRepository : CoroutineCrudRepository<ActivityParticipant, UUID> {

    fun findAllBy(pageable: Pageable): Flow<ActivityParticipant>

    @Query(
        """
        SELECT count(*)
        FROM activity_participant
        WHERE (:userId IS NULL OR user_id = :userId) AND (:postId IS NULL OR post_id = :postId)
        """
    )
    suspend fun count(userId: UUID?, postId: UUID?): Long

    @Query(
        """
        SELECT *
        FROM activity_participant
        WHERE (:userId IS NULL OR user_id = :userId) AND (:postId IS NULL OR post_id = :postId)
        OFFSET :offset LIMIT :limit
        """
    )
    fun findAllBy(userId: UUID?, postId: UUID?, offset: Long, limit: Int): Flow<ActivityParticipant>

    suspend fun deleteAllByUserId(userId: UUID)
}
