package com.uniplat.uniplatapi.repository

import com.uniplat.uniplatapi.domain.model.ClubDTO
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
class ClubDTORepository(private val databaseTemplate: R2dbcEntityOperations) {

    fun findAllBy(userId: UUID, universityId: UUID?, adminId: UUID?, offset: Long, limit: Int): Flow<ClubDTO> {
        val query = """
            SELECT *,
                   exists(
                       SELECT *
                       FROM user_follow
                       WHERE user_id = :userId AND follow_type = 'CLUB' AND follow_id = club.id
                       ) AS followed_by_user,
                   (SELECT count(*) FROM user_follow WHERE follow_type = 'CLUB' AND follow_id = club.id) AS count_follower
            FROM club
            WHERE (:universityId IS NULL OR university_id = :universityId) AND (:adminId IS NULL OR admin_id = :adminId)
            OFFSET :offset LIMIT :limit
        """.trimIndent()

        return databaseTemplate.databaseClient
            .sql(query)
            .bind("userId", userId)
            .bind("universityId", universityId)
            .bind("adminId", adminId)
            .bind("offset", offset)
            .bind("limit", limit)
            .map { row, _ -> mapClubDTO(row) }
            .all()
            .asFlow()
    }

    suspend fun findById(id: UUID, userId: UUID): ClubDTO? {
        val query = """
            SELECT *,
                   exists(
                       SELECT *
                       FROM user_follow
                       WHERE user_id = :userId AND follow_type = 'CLUB' AND follow_id = club.id
                       ) AS followed_by_user,
                   (SELECT count(*) FROM user_follow WHERE follow_type = 'CLUB' AND follow_id = club.id) AS count_follower
            FROM club
            WHERE id = :id
        """.trimIndent()

        return databaseTemplate.databaseClient
            .sql(query)
            .bind("id", id)
            .bind("userId", userId)
            .map { row, _ -> mapClubDTO(row) }
            .awaitOneOrNull()
    }

    private fun mapClubDTO(row: Row): ClubDTO {
        return ClubDTO(
            id = row.get("id", UUID::class.javaObjectType)!!,
            name = row.get("name", String::class.javaObjectType)!!,
            universityId = row.get("university_id", UUID::class.javaObjectType)!!,
            description = row.get("description", String::class.javaObjectType),
            profileImgId = row.get("profile_img_id", UUID::class.javaObjectType),
            adminId = row.get("admin_id", UUID::class.javaObjectType)!!,
            version = row.get("version", Int::class.javaObjectType)!!,
            createdAt = row.get("created_at", Instant::class.javaObjectType)!!,
            lastModifiedAt = row.get("last_modified_at", Instant::class.javaObjectType)!!,
            followedByUser = row.get("followed_by_user", Boolean::class.javaObjectType)!!,
            countFollower = row.get("count_follower", Long::class.javaObjectType)!!
        )
    }
}
