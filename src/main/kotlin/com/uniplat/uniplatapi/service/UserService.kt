package com.uniplat.uniplatapi.service

import com.uniplat.uniplatapi.domain.dto.request.CreateUserRequest
import com.uniplat.uniplatapi.domain.dto.request.UpdateUserRequest
import com.uniplat.uniplatapi.domain.enums.UserType
import com.uniplat.uniplatapi.domain.model.User
import com.uniplat.uniplatapi.exception.ConflictException
import com.uniplat.uniplatapi.exception.NotFoundException
import com.uniplat.uniplatapi.exception.OptimisticLockException
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
        with(request) {
            val user = getById(id)

            if (user.version != version) throw OptimisticLockException()

            name?.let { user.name = it }
            surname?.let { user.surname = it }
            gender?.let { user.gender = it }
            birthDate?.let { user.birthDate = it }
            password?.let { user.password = it }
            universityId?.let { user.universityId = it }
            description?.let { user.description = it }
            profileImgId?.let { user.profileImgId = it }
            messageAccessed?.let { user.messageAccessed = it }

            return userRepository.save(user)
        }
    }

    suspend fun delete(id: UUID) {
        userRepository.deleteById(id)
    }
}
