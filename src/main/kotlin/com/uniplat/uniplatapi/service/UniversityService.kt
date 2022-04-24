package com.uniplat.uniplatapi.service

import com.uniplat.uniplatapi.domain.dto.request.create.CreateUniversityRequest
import com.uniplat.uniplatapi.domain.dto.request.update.UpdateUniversityRequest
import com.uniplat.uniplatapi.domain.enums.UserType
import com.uniplat.uniplatapi.domain.model.University
import com.uniplat.uniplatapi.exception.BadRequestException
import com.uniplat.uniplatapi.exception.ConflictException
import com.uniplat.uniplatapi.exception.NotFoundException
import com.uniplat.uniplatapi.extensions.saveUnique
import com.uniplat.uniplatapi.model.PaginatedModel
import com.uniplat.uniplatapi.repository.UniversityRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UniversityService(
    private val universityRepository: UniversityRepository,
    private val userService: UserService
) {

    suspend fun getAll(pageable: Pageable): PaginatedModel<University> {
        val count = universityRepository.count()
        val universities = universityRepository.findAllBy(pageable)

        return PaginatedModel(
            content = universities,
            number = pageable.pageNumber,
            size = pageable.pageSize,
            totalElements = count
        )
    }

    suspend fun getById(id: UUID): University {
        return universityRepository.findById(id) ?: throw NotFoundException("error.university.not-found", args = listOf(id))
    }

    suspend fun create(request: CreateUniversityRequest): University {
        with(request) {
            validateAdmin(adminId)

            val university = University(
                name = name,
                description = description,
                profileImgId = profileImgId,
                adminId = adminId
            )

            return universityRepository.saveUnique(university) { throw ConflictException("error.university.conflict", args = listOf(name)) }
        }
    }

    suspend fun update(id: UUID, request: UpdateUniversityRequest): University {
        with(request) {
            adminId?.let { validateAdmin(it) }

            val university = getById(id)
            name?.let { university.name = it }
            description?.let { university.description = it }
            profileImgId?.let { university.profileImgId = it }
            adminId?.let { university.adminId = it }
            university.version = version

            return universityRepository.save(university)
        }
    }

    suspend fun delete(id: UUID) {
        universityRepository.deleteById(id)
    }

    private suspend fun validateAdmin(id: UUID) {
        val user = userService.getById(id)
        if (user.type != UserType.TEACHER) {
            throw BadRequestException("error.university.type-invalid", args = listOf(id))
        }
    }
}
