package com.uniplat.uniplatapi.validation

import com.uniplat.uniplatapi.domain.enums.UserType
import com.uniplat.uniplatapi.exception.BadRequestException
import com.uniplat.uniplatapi.service.UserService
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UniversityValidator(private val userService: UserService) {

    suspend fun validate(adminId: UUID) {
        val user = userService.getById(adminId)
        if (user.type != UserType.TEACHER) {
            throw BadRequestException("error.university.type-invalid", args = listOf(adminId))
        }
    }
}
