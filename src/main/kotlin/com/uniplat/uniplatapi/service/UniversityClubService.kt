package com.uniplat.uniplatapi.service

import com.uniplat.uniplatapi.domain.dto.request.create.CreateUniversityClubRequest
import com.uniplat.uniplatapi.domain.model.UniversityClub
import com.uniplat.uniplatapi.exception.ConflictException
import com.uniplat.uniplatapi.exception.NotFoundException
import com.uniplat.uniplatapi.extensions.saveUnique
import com.uniplat.uniplatapi.model.PaginatedModel
import com.uniplat.uniplatapi.repository.UniversityClubRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UniversityClubService(private val universityClubRepository: UniversityClubRepository) {

    suspend fun getAll(universityId: UUID?, clubId: UUID?, pageable: Pageable): PaginatedModel<UniversityClub> {
        val count = universityClubRepository.count(universityId, clubId)
        val universityClubs = universityClubRepository.findAllBy(universityId, clubId, pageable.offset, pageable.pageSize)

        return PaginatedModel(
            content = universityClubs,
            number = pageable.pageNumber,
            size = pageable.pageSize,
            totalElements = count
        )
    }

    suspend fun getById(id: UUID): UniversityClub {
        return universityClubRepository.findById(id) ?: throw NotFoundException("error.university-club.not-found", args = listOf(id))
    }

    suspend fun create(request: CreateUniversityClubRequest): UniversityClub {
        with(request) {
            val universityClub = UniversityClub(
                universityId = universityId,
                clubId = clubId
            )

            return universityClubRepository.saveUnique(universityClub) {
                throw ConflictException(
                    "error.university-club.conflict",
                    args = listOf(universityId, clubId)
                )
            }
        }
    }

    suspend fun delete(id: UUID) {
        universityClubRepository.deleteById(id)
    }
}
