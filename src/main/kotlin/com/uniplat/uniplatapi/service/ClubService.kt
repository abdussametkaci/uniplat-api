package com.uniplat.uniplatapi.service

import com.uniplat.uniplatapi.domain.dto.request.create.CreateClubRequest
import com.uniplat.uniplatapi.domain.dto.request.update.UpdateClubRequest
import com.uniplat.uniplatapi.domain.model.Club
import com.uniplat.uniplatapi.domain.model.ClubDTO
import com.uniplat.uniplatapi.exception.ConflictException
import com.uniplat.uniplatapi.exception.NotFoundException
import com.uniplat.uniplatapi.extensions.saveUnique
import com.uniplat.uniplatapi.model.PaginatedModel
import com.uniplat.uniplatapi.repository.ClubDTORepository
import com.uniplat.uniplatapi.repository.ClubRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ClubService(
    private val clubRepository: ClubRepository,
    private val clubDTORepository: ClubDTORepository
) {

    suspend fun getAll(universityId: UUID?, pageable: Pageable): PaginatedModel<Club> {
        val count = clubRepository.count(universityId)
        val clubs = clubRepository.findAllBy(universityId, pageable.offset, pageable.pageSize)

        return PaginatedModel(
            content = clubs,
            number = pageable.pageNumber,
            size = pageable.pageSize,
            totalElements = count
        )
    }

    suspend fun getAllDTO(userId: UUID, universityId: UUID?, pageable: Pageable): PaginatedModel<ClubDTO> {
        val count = clubRepository.count(universityId)
        val clubs = clubDTORepository.findAllBy(userId, universityId, pageable.offset, pageable.pageSize)

        return PaginatedModel(
            content = clubs,
            number = pageable.pageNumber,
            size = pageable.pageSize,
            totalElements = count
        )
    }

    suspend fun getByIdDTO(id: UUID, userId: UUID): ClubDTO {
        return clubDTORepository.findById(id, userId) ?: throw NotFoundException("error.club.not-found", args = listOf(id))
    }

    suspend fun getById(id: UUID): Club {
        return clubRepository.findById(id) ?: throw NotFoundException("error.club.not-found", args = listOf(id))
    }

    suspend fun create(request: CreateClubRequest): Club {
        with(request) {
            val club = Club(
                name = name,
                universityId = universityId,
                description = description,
                profileImgId = profileImgId,
                adminId = adminId
            )

            return clubRepository.saveUnique(club) { throw ConflictException("error.club.conflict", args = listOf(name, universityId)) }
        }
    }

    suspend fun update(id: UUID, request: UpdateClubRequest): Club {
        return getById(id)
            .apply {
                with(request) {
                    this@apply.name = name
                    this@apply.description = description
                    this@apply.profileImgId = profileImgId
                    this@apply.adminId = adminId
                    this@apply.version = version
                }
            }
            .let { clubRepository.saveUnique(it) { throw ConflictException("error.club.conflict", args = listOf(it.name, it.universityId)) } }
    }

    suspend fun delete(id: UUID) {
        clubRepository.deleteById(id)
    }
}
