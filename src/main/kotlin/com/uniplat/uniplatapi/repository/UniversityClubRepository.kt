package com.uniplat.uniplatapi.repository

import com.uniplat.uniplatapi.domain.model.UniversityClub
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface UniversityClubRepository : CoroutineCrudRepository<UniversityClub, UUID> {

    fun findAllBy(pageable: Pageable): Flow<UniversityClub>

    @Query(
        """
        SELECT count(*)
        FROM university_club
        WHERE (:universityId IS NULL OR university_id = :universityId) AND ((:clubId IS NULL OR club_id = :clubId))
        """
    )
    suspend fun count(universityId: UUID?, clubId: UUID?): Long

    @Query(
        """
        SELECT *
        FROM university_club
        WHERE (:universityId IS NULL OR university_id = :universityId) AND ((:clubId IS NULL OR club_id = :clubId))
        OFFSET :offset LIMIT :limit
        """
    )
    fun findAllBy(universityId: UUID?, clubId: UUID?, offset: Long, limit: Int): Flow<UniversityClub>
}
