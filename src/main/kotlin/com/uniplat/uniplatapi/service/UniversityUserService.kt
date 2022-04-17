package com.uniplat.uniplatapi.service

import com.uniplat.uniplatapi.domain.dto.request.create.CreateUniversityUserRequest
import com.uniplat.uniplatapi.domain.dto.request.update.UpdateUniversityUserRequest
import com.uniplat.uniplatapi.domain.model.UniversityUser
import com.uniplat.uniplatapi.exception.ConflictException
import com.uniplat.uniplatapi.exception.NotFoundException
import com.uniplat.uniplatapi.extensions.saveUnique
import com.uniplat.uniplatapi.model.PaginatedModel
import com.uniplat.uniplatapi.repository.UniversityUserRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UniversityUserService(private val universityUserRepository: UniversityUserRepository) {

    suspend fun getAll(pageable: Pageable): PaginatedModel<UniversityUser> {
        val count = universityUserRepository.count()
        val universityUsers = universityUserRepository.findAll(pageable)

        return PaginatedModel(
            content = universityUsers,
            number = pageable.pageNumber,
            size = pageable.pageSize,
            totalElements = count
        )
    }

    suspend fun getById(id: UUID): UniversityUser {
        return universityUserRepository.findById(id) ?: throw NotFoundException("error.university-user.not-found", args = listOf(id))
    }

    suspend fun create(request: CreateUniversityUserRequest): UniversityUser {
        with(request) {
            val universityUser = UniversityUser(
                universityId = universityId,
                userId = userId
            )

            return universityUserRepository.saveUnique(universityUser) {
                throw ConflictException(
                    "error.university-user.conflict",
                    args = listOf(universityId, userId)
                )
            }
        }
    }

    suspend fun update(id: UUID, request: UpdateUniversityUserRequest): UniversityUser {
        with(request) {
            val universityUser = getById(id)
            universityId?.let { universityUser.universityId = it }
            userId?.let { universityUser.userId = it }
            universityUser.version = version

            return universityUserRepository.save(universityUser)
        }
    }

    suspend fun delete(id: UUID) {
        universityUserRepository.deleteById(id)
    }
}
