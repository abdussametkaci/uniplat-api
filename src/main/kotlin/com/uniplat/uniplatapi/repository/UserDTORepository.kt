package com.uniplat.uniplatapi.repository

import com.uniplat.uniplatapi.domain.enums.Gender
import com.uniplat.uniplatapi.domain.enums.UserType
import com.uniplat.uniplatapi.domain.model.UserDTO
import io.r2dbc.spi.Row
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.springframework.data.r2dbc.core.R2dbcEntityOperations
import org.springframework.r2dbc.core.awaitOneOrNull
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.UUID

@Repository
class UserDTORepository(private val databaseTemplate: R2dbcEntityOperations) {

    fun findAllBy(userId: UUID, offset: Long, limit: Int): Flow<UserDTO> {
        val query = """
            SELECT *,
                   exists(
                           SELECT *
                           FROM user_follow
                           WHERE user_id = :userId AND follow_type = 'USER' AND follow_id = u.id
                       ) AS followed_by_user,
                   (SELECT count(*) FROM user_follow WHERE follow_type = 'USER' AND follow_id = u.id) AS count_follower,
                   (SELECT count(*) FROM user_follow WHERE user_id = u.id) AS count_follow
            FROM "user" u
            OFFSET :offset LIMIT :limit
        """.trimIndent()

        return databaseTemplate.databaseClient
            .sql(query)
            .bind("userId", userId)
            .bind("offset", offset)
            .bind("limit", limit)
            .map(::mapUserDTO)
            .all()
            .asFlow()
    }

    suspend fun findById(id: UUID, userId: UUID): UserDTO? {
        val query = """
            SELECT *,
                   exists(
                           SELECT *
                           FROM user_follow
                           WHERE user_id = :userId AND follow_type = 'USER' AND follow_id = u.id
                       ) AS followed_by_user,
                   (SELECT count(*) FROM user_follow WHERE follow_type = 'USER' AND follow_id = u.id) AS count_follower,
                   (SELECT count(*) FROM user_follow WHERE user_id = u.id) AS count_follow
            FROM "user" u
            WHERE id = :id
        """.trimIndent()

        return databaseTemplate.databaseClient
            .sql(query)
            .bind("id", id)
            .bind("userId", userId)
            .map(::mapUserDTO)
            .awaitOneOrNull()
    }

    private fun mapUserDTO(row: Row): UserDTO {
        return UserDTO(
            id = row.get("id", UUID::class.javaObjectType)!!,
            name = row.get("name", String::class.javaObjectType)!!,
            surname = row.get("surname", String::class.javaObjectType)!!,
            gender = row.get("gender", Gender::class.javaObjectType)!!,
            birthDate = row.get("birth_date", Instant::class.javaObjectType)!!,
            email = row.get("email", String::class.javaObjectType)!!,
            password = row.get("password", String::class.javaObjectType)!!,
            universityId = row.get("university_id", UUID::class.javaObjectType),
            type = row.get("type", UserType::class.javaObjectType)!!,
            description = row.get("description", String::class.javaObjectType),
            profileImgId = row.get("profile_img_id", UUID::class.javaObjectType),
            messageAccessed = row.get("message_accessed", Boolean::class.javaObjectType)!!,
            version = row.get("version", Int::class.javaObjectType)!!,
            createdAt = row.get("created_at", Instant::class.javaObjectType)!!,
            lastModifiedAt = row.get("last_modified_at", Instant::class.javaObjectType)!!,
            followedByUser = row.get("followed_by_user", Boolean::class.javaObjectType)!!,
            countFollower = row.get("count_follower", Long::class.javaObjectType)!!,
            countFollow = row.get("count_follow", Long::class.javaObjectType)!!
        )
    }
}
