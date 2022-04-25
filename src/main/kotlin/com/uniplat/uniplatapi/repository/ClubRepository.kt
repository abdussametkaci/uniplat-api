package com.uniplat.uniplatapi.repository

import com.uniplat.uniplatapi.domain.model.Club
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface ClubRepository : CoroutineCrudRepository<Club, UUID> {

    fun findAllBy(pageable: Pageable): Flow<Club>

    @Query(
        """
        SELECT count(*)
        FROM club
        WHERE (:universityId IS NULL OR university_id = :universityId)
        """
    )
    suspend fun count(universityId: UUID?): Long

    @Query(
        """
        SELECT *
        FROM club
        WHERE (:universityId IS NULL OR university_id = :universityId)
        OFFSET :offset LIMIT :limit
        """
    )
    fun findAllBy(universityId: UUID?, offset: Long, limit: Int): Flow<Club>
}
