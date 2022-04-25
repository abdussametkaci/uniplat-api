package com.uniplat.uniplatapi.repository

import com.uniplat.uniplatapi.domain.enums.PostOwnerType
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
        WHERE (:ownerId IS NULL OR owner_id = :ownerId) AND ((:postOwnerType IS NULL OR post_owner_type = :postOwnerType))
        """
    )
    suspend fun count(ownerId: UUID?, postOwnerType: PostOwnerType?): Long

    @Query(
        """
        SELECT *
        FROM post
        WHERE (:ownerId IS NULL OR owner_id = :ownerId) AND ((:postOwnerType IS NULL OR post_owner_type = :postOwnerType))
        OFFSET :offset LIMIT :limit
        """
    )
    fun findAllBy(ownerId: UUID?, postOwnerType: PostOwnerType?, offset: Long, limit: Int): Flow<Post>
}
