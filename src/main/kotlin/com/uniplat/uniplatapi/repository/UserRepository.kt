package com.uniplat.uniplatapi.repository

import com.uniplat.uniplatapi.domain.enums.UserType
import com.uniplat.uniplatapi.domain.model.User
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface UserRepository : CoroutineCrudRepository<User, UUID> {

    suspend fun findByEmailAndPassword(email: String, password: String): User?

    fun findAllBy(pageable: Pageable): Flow<User>

    @Query(
        """
        SELECT *
        FROM "user"
        WHERE full_text @@ to_tsquery('simple', :text)
        OFFSET :offset LIMIT :limit
        """
    )
    fun findAllBy(text: String, offset: Long, limit: Int): Flow<User>

    @Query(
        """
        SELECT *
        FROM "user"
        WHERE full_text @@ to_tsquery('simple', :text || ':*')
        OFFSET :offset LIMIT :limit
        """
    )
    fun findAllByStartWith(text: String, offset: Long, limit: Int): Flow<User>

    @Query(
        """
        SELECT *
        FROM "user"
        WHERE full_text @@ to_tsquery('simple', :text)
        AND type = :type
        OFFSET :offset LIMIT :limit
        """
    )
    fun findAllByType(type: UserType, text: String, offset: Long, limit: Int): Flow<User>

    @Query(
        """
        SELECT *
        FROM "user"
        WHERE full_text @@ to_tsquery('simple', :text || ':*')
        AND type = :type
        OFFSET :offset LIMIT :limit
        """
    )
    fun findAllByTypeStartWith(type: UserType, text: String, offset: Long, limit: Int): Flow<User>

    @Query(
        """
        SELECT count(*)
        FROM "user"
        WHERE full_text @@ to_tsquery('simple', :text)
        """
    )
    suspend fun count(text: String): Long

    @Query(
        """
        SELECT count(*)
        FROM "user"
        WHERE full_text @@ to_tsquery('simple', :text || ':*')
        """
    )
    suspend fun countByStartWith(text: String): Long

    @Query(
        """
        SELECT count(*)
        FROM "user"
        WHERE full_text @@ to_tsquery('simple', :text)
        AND type = :type
        """
    )
    suspend fun countByType(type: UserType, text: String): Long

    @Query(
        """
        SELECT count(*)
        FROM "user"
        WHERE full_text @@ to_tsquery('simple', :text || ':*')
        AND type = :type
        """
    )
    suspend fun countByTypeStartWith(type: UserType, text: String): Long

    @Query(
        """
        DELETE FROM "user"
               WHERE id = :id
               RETURNING *
        """
    )
    suspend fun deleteAndReturnById(id: UUID): User?

    suspend fun countByEnabledIsTrue(): Long
}
