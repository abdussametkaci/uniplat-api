package com.uniplat.uniplatapi.repository

import com.uniplat.uniplatapi.domain.enums.OwnerType
import com.uniplat.uniplatapi.domain.enums.PostType
import com.uniplat.uniplatapi.domain.model.PostDTO
import io.r2dbc.spi.Row
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.springframework.data.r2dbc.core.R2dbcEntityOperations
import org.springframework.r2dbc.core.awaitOneOrNull
import org.springframework.r2dbc.core.bind
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.UUID

@Repository
class PostDTORepository(private val databaseTemplate: R2dbcEntityOperations) {

    fun findAllBy(userId: UUID, ownerId: UUID?, ownerType: OwnerType?, offset: Long, limit: Int): Flow<PostDTO> {
        val query = """
            SELECT *,
                   exists(SELECT * FROM user_liked_post WHERE user_id = :userId AND post_id = post.id) AS liked_by_user,
                   (SELECT count(*) FROM user_liked_post WHERE post_id = post.id) AS count_like,
                   (CASE WHEN post.post_type = 'ACTIVITY' THEN (
                        exists(SELECT *
                               FROM activity_participant ap
                               WHERE ap.user_id = :userId AND ap.post_id = post.id
                            )
                       )
                   END) AS activity_participated_by_user,
                   (CASE WHEN post.post_type = 'ACTIVITY' THEN (
                       SELECT count(*)
                       FROM activity_participant ap
                       WHERE ap.post_id = post.id
                       )
                   END) AS activity_count_participant
            FROM post
            WHERE (:ownerId IS NULL OR owner_id = :ownerId) AND (:ownerType IS NULL OR owner_type = :ownerType)
            OFFSET :offset LIMIT :limit
        """.trimIndent()

        return databaseTemplate.databaseClient
            .sql(query)
            .bind("userId", userId)
            .bind("ownerId", ownerId)
            .bind("ownerType", ownerType)
            .bind("offset", offset)
            .bind("limit", limit)
            .map(::mapPostDTO)
            .all()
            .asFlow()
    }

    suspend fun findById(id: UUID, userId: UUID): PostDTO? {
        val query = """
            SELECT *,
                   exists(SELECT * FROM user_liked_post WHERE user_id = :userId AND post_id = post.id) AS liked_by_user,
                   (SELECT count(*) FROM user_liked_post WHERE post_id = post.id) AS count_like,
                   (CASE WHEN post.post_type = 'ACTIVITY' THEN (
                        exists(SELECT *
                               FROM activity_participant ap
                               WHERE ap.user_id = :userId AND ap.post_id = post.id
                            )
                       )
                   END) AS activity_participated_by_user,
                   (CASE WHEN post.post_type = 'ACTIVITY' THEN (
                       SELECT count(*)
                       FROM activity_participant ap
                       WHERE ap.post_id = post.id
                       )
                   END) AS activity_count_participant
            FROM post
            WHERE post.id = :id
        """.trimIndent()

        return databaseTemplate.databaseClient
            .sql(query)
            .bind("id", id)
            .bind("userId", userId)
            .map(::mapPostDTO)
            .awaitOneOrNull()
    }

    fun postFlowByUserId(userId: UUID, offset: Long, limit: Int): Flow<PostDTO> {
        val query = """
            SELECT *, 
                   exists(SELECT * FROM user_liked_post WHERE user_id = :userId AND post_id = post.id) AS liked_by_user, 
                   (SELECT count(*) FROM user_liked_post WHERE post_id = post.id) AS count_like,
                   (CASE WHEN post.post_type = 'ACTIVITY' THEN (
                        exists(SELECT *
                               FROM activity_participant ap
                               WHERE ap.user_id = :userId AND ap.post_id = post.id
                            )
                       )
                   END) AS activity_participated_by_user,
                   (CASE WHEN post.post_type = 'ACTIVITY' THEN (
                       SELECT count(*)
                       FROM activity_participant ap
                       WHERE ap.post_id = post.id
                       )
                   END) AS activity_count_participant
            FROM post
            WHERE (owner_type, owner_id) IN (
                SELECT follow_type, follow_id
                FROM user_follow
                WHERE user_id = :userId
            )
            ORDER BY post.created_at DESC
            OFFSET :offset LIMIT :limit
        """.trimIndent()

        return databaseTemplate.databaseClient
            .sql(query)
            .bind("userId", userId)
            .bind("offset", offset)
            .bind("limit", limit)
            .map(::mapPostDTO)
            .all()
            .asFlow()
    }

    private fun mapPostDTO(row: Row): PostDTO {
        return PostDTO(
            id = row.get("id", UUID::class.javaObjectType)!!,
            imgId = row.get("img_id", UUID::class.javaObjectType),
            description = row.get("description", String::class.javaObjectType),
            ownerType = row.get("owner_type", OwnerType::class.javaObjectType)!!,
            postType = row.get("post_type", PostType::class.javaObjectType)!!,
            ownerId = row.get("owner_id", UUID::class.javaObjectType)!!,
            sharedPostId = row.get("shared_post_id", UUID::class.javaObjectType),
            activityTitle = row.get("activity_title", String::class.javaObjectType),
            activityStartAt = row.get("activity_start_at", Instant::class.javaObjectType),
            version = row.get("version", Int::class.javaObjectType)!!,
            createdAt = row.get("created_at", Instant::class.javaObjectType)!!,
            lastModifiedAt = row.get("last_modified_at", Instant::class.javaObjectType)!!,
            likedByUser = row.get("liked_by_user", Boolean::class.javaObjectType)!!,
            countLike = row.get("count_like", Long::class.javaObjectType)!!,
            activityParticipatedByUser = row.get("activity_participated_by_user", Boolean::class.javaObjectType),
            activityCountParticipant = row.get("activity_count_participant", Long::class.javaObjectType)
        )
    }
}
