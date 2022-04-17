package com.uniplat.uniplatapi.service

import com.uniplat.uniplatapi.domain.dto.request.create.CreateClubUserRequest
import com.uniplat.uniplatapi.domain.dto.request.update.UpdateClubUserRequest
import com.uniplat.uniplatapi.domain.model.ClubUser
import com.uniplat.uniplatapi.exception.ConflictException
import com.uniplat.uniplatapi.exception.NotFoundException
import com.uniplat.uniplatapi.extensions.saveUnique
import com.uniplat.uniplatapi.model.PaginatedModel
import com.uniplat.uniplatapi.repository.ClubUserRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ClubUserService(private val clubUserRepository: ClubUserRepository) {

    suspend fun getAll(pageable: Pageable): PaginatedModel<ClubUser> {
        val count = clubUserRepository.count()
        val clubUsers = clubUserRepository.findAllBy(pageable)

        return PaginatedModel(
            content = clubUsers,
            number = pageable.pageNumber,
            size = pageable.pageSize,
            totalElements = count
        )
    }

    suspend fun getById(id: UUID): ClubUser {
        return clubUserRepository.findById(id) ?: throw NotFoundException("error.club-user.not-found", args = listOf(id))
    }

    suspend fun create(request: CreateClubUserRequest): ClubUser {
        with(request) {
            val clubUser = ClubUser(
                clubId = clubId,
                userId = clubId
            )

            return clubUserRepository.saveUnique(clubUser) { throw ConflictException("error.club-user.conflict", args = listOf(clubId, userId)) }
        }
    }

    suspend fun update(id: UUID, request: UpdateClubUserRequest): ClubUser {
        with(request) {
            val clubUser = getById(id)

            clubId?.let { clubUser.clubId = it }
            userId?.let { clubUser.userId = it }
            clubUser.version = version

            return clubUserRepository.save(clubUser)
        }
    }

    suspend fun delete(id: UUID) {
        clubUserRepository.deleteById(id)
    }
}
