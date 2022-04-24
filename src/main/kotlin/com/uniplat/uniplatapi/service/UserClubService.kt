package com.uniplat.uniplatapi.service

import com.uniplat.uniplatapi.domain.dto.request.create.CreateUserClubRequest
import com.uniplat.uniplatapi.domain.model.UserClub
import com.uniplat.uniplatapi.exception.ConflictException
import com.uniplat.uniplatapi.exception.NotFoundException
import com.uniplat.uniplatapi.extensions.saveUnique
import com.uniplat.uniplatapi.model.PaginatedModel
import com.uniplat.uniplatapi.repository.UserClubRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserClubService(private val userClubRepository: UserClubRepository) {

    suspend fun getAll(pageable: Pageable): PaginatedModel<UserClub> {
        val count = userClubRepository.count()
        val userClubs = userClubRepository.findAll(pageable)

        return PaginatedModel(
            content = userClubs,
            number = pageable.pageNumber,
            size = pageable.pageSize,
            totalElements = count
        )
    }

    suspend fun getById(id: UUID): UserClub {
        return userClubRepository.findById(id) ?: throw NotFoundException("error.user-club.not-found", args = listOf(id))
    }

    suspend fun create(request: CreateUserClubRequest): UserClub {
        with(request) {
            val userClub = UserClub(
                clubId = clubId,
                userId = clubId
            )

            return userClubRepository.saveUnique(userClub) { throw ConflictException("error.user-club.conflict", args = listOf(clubId, userId)) }
        }
    }

    suspend fun delete(id: UUID) {
        userClubRepository.deleteById(id)
    }
}
