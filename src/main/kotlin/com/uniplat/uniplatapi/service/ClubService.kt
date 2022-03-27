package com.uniplat.uniplatapi.service

import com.uniplat.uniplatapi.domain.dto.request.create.CreateClubRequest
import com.uniplat.uniplatapi.domain.dto.request.update.UpdateClubRequest
import com.uniplat.uniplatapi.domain.model.Club
import com.uniplat.uniplatapi.exception.ConflictException
import com.uniplat.uniplatapi.exception.NotFoundException
import com.uniplat.uniplatapi.extensions.saveUnique
import com.uniplat.uniplatapi.model.PaginatedModel
import com.uniplat.uniplatapi.repository.ClubRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ClubService(private val clubRepository: ClubRepository) {

    suspend fun getAll(pageable: Pageable): PaginatedModel<Club> {
        val count = clubRepository.count()
        val clubs = clubRepository.findAllBy(pageable)

        return PaginatedModel(
            content = clubs,
            number = pageable.pageNumber,
            size = pageable.pageSize,
            totalElements = count
        )
    }

    suspend fun getById(id: UUID): Club {
        return clubRepository.findById(id) ?: throw NotFoundException("error.club.not-found", args = listOf(id))
    }

    suspend fun create(request: CreateClubRequest): Club {
        with(request) {
            val club = Club(
                name = name,
                universityId = universityId,
                adminId = adminId
            )

            return clubRepository.saveUnique(club) { throw ConflictException("error.club.conflict", args = listOf(name)) }
        }
    }

    suspend fun update(id: UUID, request: UpdateClubRequest): Club {
        with(request) {
            val club = getById(id)

            name?.let { club.name = it }
            adminId?.let { club.adminId = it }
            club.version = version

            return clubRepository.save(club)
        }
    }

    suspend fun delete(id: UUID) {
        clubRepository.deleteById(id)
    }
}
