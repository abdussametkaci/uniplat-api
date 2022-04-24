package com.uniplat.uniplatapi.service

import com.uniplat.uniplatapi.domain.dto.request.create.CreateUniversityUserRequest
import com.uniplat.uniplatapi.domain.model.UserUniversity
import com.uniplat.uniplatapi.exception.ConflictException
import com.uniplat.uniplatapi.extensions.saveUnique
import com.uniplat.uniplatapi.model.PaginatedModel
import com.uniplat.uniplatapi.repository.UserUniversityRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserUniversityService(private val userUniversityRepository: UserUniversityRepository) {

    suspend fun getAll(pageable: Pageable): PaginatedModel<UserUniversity> {
        val count = userUniversityRepository.count()
        val universityUsers = userUniversityRepository.findAllBy(pageable)

        return PaginatedModel(
            content = universityUsers,
            number = pageable.pageNumber,
            size = pageable.pageSize,
            totalElements = count
        )
    }

    suspend fun create(request: CreateUniversityUserRequest): UserUniversity {
        with(request) {
            val userUniversity = UserUniversity(
                userId = userId,
                universityId = universityId
            )

            return userUniversityRepository.saveUnique(userUniversity) {
                throw ConflictException(
                    "error.user-university.conflict",
                    args = listOf(userId, universityId)
                )
            }
        }
    }

    suspend fun delete(id: UUID) {
        userUniversityRepository.deleteById(id)
    }
}