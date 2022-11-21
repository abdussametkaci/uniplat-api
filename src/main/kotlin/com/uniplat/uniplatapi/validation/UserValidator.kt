package com.uniplat.uniplatapi.validation

import com.uniplat.uniplatapi.domain.dto.request.update.UpdateUserPasswordRequest
import com.uniplat.uniplatapi.domain.model.User
import com.uniplat.uniplatapi.exception.BadRequestException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class UserValidator(private val passwordEncoder: PasswordEncoder) {

    suspend fun validate(user: User, request: UpdateUserPasswordRequest) {
        with(request) {
            if (currentPassword == newPassword) throw BadRequestException("error.user.comparing-password-invalid")
            if (passwordEncoder.matches(currentPassword, user.password)) throw BadRequestException("error.user.update-password-invalid")
        }
    }
}
