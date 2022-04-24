package com.uniplat.uniplatapi.repository

import com.uniplat.uniplatapi.domain.model.UserUniversity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface UserUniversityRepository : CoroutineCrudRepository<UserUniversity, UUID> {

    fun findAllBy(pageable: Pageable): Flow<UserUniversity>

    @Query(
        """
        SELECT count(*)
        FROM user_university
        WHERE (:userId IS NULL OR user_id = :userId) AND ((:universityId IS NULL OR university_id = :universityId))
        """
    )
    suspend fun count(userId: UUID?, universityId: UUID?): Long

    @Query(
        """
        SELECT *
        FROM user_university
        WHERE (:userId IS NULL OR user_id = :userId) AND ((:universityId IS NULL OR university_id = :universityId))
        OFFSET :offset LIMIT :limit
        """
    )
    fun findAllBy(userId: UUID?, universityId: UUID?, offset: Long, limit: Int): Flow<UserUniversity>
}
