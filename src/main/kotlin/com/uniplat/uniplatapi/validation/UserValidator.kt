package com.uniplat.uniplatapi.validation

import com.uniplat.uniplatapi.domain.dto.request.update.UpdateUserPasswordRequest
import com.uniplat.uniplatapi.domain.model.User
import com.uniplat.uniplatapi.exception.BadRequestException
import org.springframework.stereotype.Component

@Component
class UserValidator {

    suspend fun validate(user: User, request: UpdateUserPasswordRequest) {
        with(request) {
            if (currentPassword == newPassword) throw BadRequestException("error.user.comparing-password-invalid")
            if (user.password != currentPassword) throw BadRequestException("error.user.update-password-invalid")
        }
    }
}
