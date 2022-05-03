package com.uniplat.uniplatapi.service

import com.uniplat.uniplatapi.domain.dto.request.create.CreateUserFollowRequest
import com.uniplat.uniplatapi.domain.enums.OwnerType
import com.uniplat.uniplatapi.domain.model.UserFollow
import com.uniplat.uniplatapi.exception.ConflictException
import com.uniplat.uniplatapi.extensions.saveUnique
import com.uniplat.uniplatapi.model.PaginatedModel
import com.uniplat.uniplatapi.repository.UserFollowRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserFollowService(private val userFollowRepository: UserFollowRepository) {

    suspend fun getAll(userId: UUID?, followType: OwnerType?, pageable: Pageable): PaginatedModel<UserFollow> {
        val count = userFollowRepository.count(userId, followType)
        val userContacts = userFollowRepository.findAllBy(userId, followType, pageable.offset, pageable.pageSize)

        return PaginatedModel(
            content = userContacts,
            number = pageable.pageNumber,
            size = pageable.pageSize,
            totalElements = count
        )
    }

    suspend fun create(request: CreateUserFollowRequest): UserFollow {
        with(request) {
            val userClub = UserFollow(
                userId = userId,
                followType = followType,
                followId = followId
            )

            return userFollowRepository.saveUnique(userClub) { throw ConflictException("error.follow.conflict", args = listOf(userId, followType, followId)) }
        }
    }

    suspend fun delete(id: UUID) {
        userFollowRepository.deleteById(id)
    }
}
