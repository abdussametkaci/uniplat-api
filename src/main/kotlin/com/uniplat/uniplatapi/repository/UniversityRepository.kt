package com.uniplat.uniplatapi.repository

import com.uniplat.uniplatapi.domain.model.University
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface UniversityRepository : CoroutineCrudRepository<University, UUID> {

    fun findAllBy(pageable: Pageable): Flow<University>

    @Query(
        """
        SELECT *
        FROM university
        WHERE full_text @@ to_tsquery('simple', :text)
        OFFSET :offset LIMIT :limit
        """
    )
    fun findAllBy(text: String, offset: Long, limit: Int): Flow<University>

    @Query(
        """
        SELECT *
        FROM university
        WHERE full_text @@ to_tsquery('simple', :text || ':*')
        OFFSET :offset LIMIT :limit
        """
    )
    fun findAllByStartWith(text: String, offset: Long, limit: Int): Flow<University>

    @Query(
        """
        SELECT count(*)
        FROM university
        WHERE full_text @@ to_tsquery('simple', :text)
        """
    )
    suspend fun count(text: String): Long

    @Query(
        """
        SELECT count(*)
        FROM university
        WHERE full_text @@ to_tsquery('simple', :text || ':*')
        """
    )
    suspend fun countByStartWith(text: String): Long

    @Query(
        """
        SELECT count(*)
        FROM university
        WHERE (:adminId IS NULL OR admin_id = :adminId)
        """
    )
    suspend fun count(adminId: UUID?): Long

    @Query(
        """
        DELETE FROM university
               WHERE id = :id
               RETURNING *
        """
    )
    suspend fun deleteAndReturnById(id: UUID): University?
}
