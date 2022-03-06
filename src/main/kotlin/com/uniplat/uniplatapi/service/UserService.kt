package com.uniplat.uniplatapi.service

import com.uniplat.uniplatapi.domain.dto.request.CreateUserRequest
import com.uniplat.uniplatapi.domain.enums.UserType
import com.uniplat.uniplatapi.domain.model.User
import com.uniplat.uniplatapi.exception.ConflictException
import com.uniplat.uniplatapi.exception.NotFoundException
import com.uniplat.uniplatapi.model.PaginatedModel
import com.uniplat.uniplatapi.repository.UserRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(private val userRepository: UserRepository) {

    suspend fun getUsers(pageable: Pageable): PaginatedModel<User> {
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
        return userRepository.findById(id) ?: throw NotFoundException("error.user.not-found", args = arrayOf(id))
    }

    suspend fun create(createUserRequest: CreateUserRequest): User {
        with(createUserRequest) {
            if (userRepository.existsByEmail(email)) {
                throw ConflictException("error.user.conflict", args = arrayOf(email))
            }

            val user = User(
                name = name,
                surname = surname,
                gender = gender,
                birthDate = birthDate,
                email = email,
                password = password,
                universityId = universityId,
                type = if ("@stu" in email) UserType.STUDENT else UserType.TEACHER
            )

            description?.let { user.description = it }
            profileImgId?.let { user.profileImgId = it }

            return userRepository.save(user)
        }
    }
}
