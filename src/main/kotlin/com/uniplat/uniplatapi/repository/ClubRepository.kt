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
        WHERE (:universityId IS NULL OR university_id = :universityId) AND (:adminId IS NULL OR admin_id = :adminId)
        """
    )
    suspend fun count(universityId: UUID?, adminId: UUID?): Long

    @Query(
        """
        SELECT *
        FROM club
        WHERE (:universityId IS NULL OR university_id = :universityId) AND (:adminId IS NULL OR admin_id = :adminId)
        OFFSET :offset LIMIT :limit
        """
    )
    fun findAllBy(universityId: UUID?, adminId: UUID?, offset: Long, limit: Int): Flow<Club>

    @Query(
        """
        SELECT *
        FROM club
        WHERE full_text @@ to_tsquery('simple', :text)
        OFFSET :offset LIMIT :limit
        """
    )
    fun findAllBy(text: String, offset: Long, limit: Int): Flow<Club>

    @Query(
        """
        SELECT *
        FROM club
        WHERE full_text @@ to_tsquery('simple', :text || ':*')
        OFFSET :offset LIMIT :limit
        """
    )
    fun findAllByNonWord(text: String, offset: Long, limit: Int): Flow<Club>

    @Query(
        """
        SELECT count(*)
        FROM club
        WHERE full_text @@ to_tsquery('simple', :text)
        """
    )
    suspend fun count(text: String): Long

    @Query(
        """
        SELECT count(*)
        FROM club
        WHERE full_text @@ to_tsquery('simple', :text || ':*')
        """
    )
    suspend fun countByNonWord(text: String): Long
}
