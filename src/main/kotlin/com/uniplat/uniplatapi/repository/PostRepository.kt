package com.uniplat.uniplatapi.repository

import com.uniplat.uniplatapi.domain.enums.OwnerType
import com.uniplat.uniplatapi.domain.model.Post
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface PostRepository : CoroutineCrudRepository<Post, UUID> {

    fun findAllBy(pageable: Pageable): Flow<Post>

    @Query(
        """
        SELECT count(*)
        FROM post
        WHERE (:ownerId IS NULL OR owner_id = :ownerId) AND (:ownerType IS NULL OR owner_type = :ownerType)
        """
    )
    suspend fun count(ownerId: UUID?, ownerType: OwnerType?): Long

    @Query(
        """
        SELECT *
        FROM post
        WHERE (:ownerId IS NULL OR owner_id = :ownerId) AND (:ownerType IS NULL OR owner_type = :ownerType)
        OFFSET :offset LIMIT :limit
        """
    )
    fun findAllBy(ownerId: UUID?, ownerType: OwnerType?, offset: Long, limit: Int): Flow<Post>
}
