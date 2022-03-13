package com.uniplat.uniplatapi.service

import com.uniplat.uniplatapi.domain.model.User
import com.uniplat.uniplatapi.exception.BadRequestException
import com.uniplat.uniplatapi.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class LoginService(private val userRepository: UserRepository) {

    suspend fun login(email: String, password: String): User {
        return userRepository.findByEmailAndPassword(email, password) ?: throw BadRequestException("error.login.invalid")
    }
}
