package com.uniplat.uniplatapi.repository

import com.uniplat.uniplatapi.domain.model.UserClub
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface UserClubRepository : CoroutineCrudRepository<UserClub, UUID> {

    fun findAllBy(pageable: Pageable): Flow<UserClub>

    @Query(
        """
        SELECT count(*)
        FROM user_club
        WHERE (:userId IS NULL OR user_id = :userId) AND (:clubId IS NULL OR club_id = :clubId)
        """
    )
    suspend fun count(userId: UUID?, clubId: UUID?): Long

    @Query(
        """
        SELECT *
        FROM user_club
        WHERE (:userId IS NULL OR user_id = :userId) AND (:clubId IS NULL OR club_id = :clubId)
        OFFSET :offset LIMIT :limit
        """
    )
    fun findAllBy(userId: UUID?, clubId: UUID?, offset: Long, limit: Int): Flow<UserClub>
}
