package com.uniplat.uniplatapi.service

import com.uniplat.uniplatapi.domain.dto.request.create.CreateUserRequest
import com.uniplat.uniplatapi.domain.dto.request.update.UpdateUserPasswordRequest
import com.uniplat.uniplatapi.domain.dto.request.update.UpdateUserRequest
import com.uniplat.uniplatapi.domain.enums.UserType
import com.uniplat.uniplatapi.domain.model.User
import com.uniplat.uniplatapi.exception.BadRequestException
import com.uniplat.uniplatapi.exception.ConflictException
import com.uniplat.uniplatapi.exception.NotFoundException
import com.uniplat.uniplatapi.extensions.saveUnique
import com.uniplat.uniplatapi.model.PaginatedModel
import com.uniplat.uniplatapi.repository.UserRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(private val userRepository: UserRepository) {

    suspend fun getAll(pageable: Pageable): PaginatedModel<User> {
        val count = userRepository.count()
        val users = userRepository.findAllBy(pageable)

        return PaginatedModel(
            content = users,
            number = pageable.pageNumber,
            size = pageable.pageSize,
            totalElements = count
        )
    }

    suspend fun getById(id: UUID): User {
        return userRepository.findById(id) ?: throw NotFoundException("error.user.not-found", args = listOf(id))
    }

    suspend fun create(request: CreateUserRequest): User {
        with(request) {
            val user = User(
                name = name,
                surname = surname,
                gender = gender,
                birthDate = birthDate,
                email = email,
                password = password,
                universityId = universityId,
                type = if ("@stu" in email) UserType.STUDENT else UserType.TEACHER,
                description = description,
                profileImgId = profileImgId,
                messageAccessed = messageAccessed
            )

            return userRepository.saveUnique(user) { throw ConflictException("error.user.conflict", args = listOf(email)) }
        }
    }

    suspend fun update(id: UUID, request: UpdateUserRequest): User {
        return getById(id)
            .apply {
                with(request) {
                    this@apply.name = name
                    this@apply.surname = surname
                    this@apply.gender = gender
                    this@apply.birthDate = birthDate
                    this@apply.universityId = universityId
                    this@apply.description = description
                    this@apply.profileImgId = profileImgId
                    this@apply.messageAccessed = messageAccessed
                    this@apply.version = version
                }
            }
            .let { userRepository.saveUnique(it) { throw ConflictException("error.user.conflict", args = listOf(it.email)) } }
    }

    suspend fun updatePassword(id: UUID, request: UpdateUserPasswordRequest): User {
        return getById(id)
            .also { validate(it, request) }
            .apply {
                with(request) {
                    this@apply.password = newPassword
                    this@apply.version = version
                }
            }
            .let { userRepository.saveUnique(it) { throw ConflictException("error.user.conflict", args = listOf(it.email)) } }
    }

    suspend fun delete(id: UUID) {
        userRepository.deleteById(id)
    }

    private suspend fun validate(user: User, request: UpdateUserPasswordRequest) {
        with(request) {
            if (currentPassword == newPassword) throw BadRequestException("error.user.comparing-password-invalid")
            if (user.password != currentPassword) throw BadRequestException("error.user.update-password-invalid")
        }
    }
}
