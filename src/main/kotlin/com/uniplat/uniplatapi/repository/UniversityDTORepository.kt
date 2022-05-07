package com.uniplat.uniplatapi.repository

import com.uniplat.uniplatapi.domain.model.UniversityDTO
import io.r2dbc.spi.Row
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.springframework.data.r2dbc.core.R2dbcEntityOperations
import org.springframework.r2dbc.core.awaitOneOrNull
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.UUID

@Repository
class UniversityDTORepository(private val databaseTemplate: R2dbcEntityOperations) {

    fun findAllBy(userId: UUID, offset: Long, limit: Int): Flow<UniversityDTO> {
        val query = """
            SELECT *,
                   exists(
                           SELECT *
                           FROM user_follow
                           WHERE user_id = :userId AND follow_type = 'UNIVERSITY' AND follow_id = university.id
                       ) AS followed_by_user,
                   (SELECT count(*) FROM user_follow WHERE follow_type = 'UNIVERSITY' AND follow_id = university.id) AS count_follower,
                   (SELECT count(*) FROM club WHERE university_id = university.id) AS count_club
            FROM university
            OFFSET :offset LIMIT :limit
        """.trimIndent()

        return databaseTemplate.databaseClient
            .sql(query)
            .bind("userId", userId)
            .bind("offset", offset)
            .bind("limit", limit)
            .map(::mapUniversityDTO)
            .all()
            .asFlow()
    }

    suspend fun findById(id: UUID, userId: UUID): UniversityDTO? {
        val query = """
            SELECT *,
                   exists(
                           SELECT *
                           FROM user_follow
                           WHERE user_id = :userId AND follow_type = 'UNIVERSITY' AND follow_id = university.id
                       ) AS followed_by_user,
                   (SELECT count(*) FROM user_follow WHERE follow_type = 'UNIVERSITY' AND follow_id = university.id) AS count_follower,
                   (SELECT count(*) FROM club WHERE university_id = university.id) AS count_club
            FROM university
            WHERE id = :id
        """.trimIndent()

        return databaseTemplate.databaseClient
            .sql(query)
            .bind("id", id)
            .bind("userId", userId)
            .map(::mapUniversityDTO)
            .awaitOneOrNull()
    }

    private fun mapUniversityDTO(row: Row): UniversityDTO {
        return UniversityDTO(
            id = row.get("id", UUID::class.javaObjectType)!!,
            name = row.get("name", String::class.javaObjectType)!!,
            description = row.get("description", String::class.javaObjectType),
            profileImgId = row.get("profile_img_id", UUID::class.javaObjectType),
            adminId = row.get("admin_id", UUID::class.javaObjectType)!!,
            version = row.get("version", Int::class.javaObjectType)!!,
            createdAt = row.get("created_at", Instant::class.javaObjectType)!!,
            lastModifiedAt = row.get("last_modified_at", Instant::class.javaObjectType)!!,
            followedByUser = row.get("followed_by_user", Boolean::class.javaObjectType)!!,
            countFollower = row.get("count_follower", Long::class.javaObjectType)!!,
            countClub = row.get("count_club", Long::class.javaObjectType)!!
        )
    }
}
