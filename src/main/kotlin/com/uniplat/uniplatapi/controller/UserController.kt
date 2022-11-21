package com.uniplat.uniplatapi.controller

import com.uniplat.uniplatapi.domain.dto.request.create.CreateUserRequest
import com.uniplat.uniplatapi.domain.dto.request.update.UpdateUserPasswordRequest
import com.uniplat.uniplatapi.domain.dto.request.update.UpdateUserRequest
import com.uniplat.uniplatapi.domain.dto.response.UserDTOResponse
import com.uniplat.uniplatapi.domain.dto.response.UserResponse
import com.uniplat.uniplatapi.domain.model.User
import com.uniplat.uniplatapi.extensions.convert
import com.uniplat.uniplatapi.extensions.convertWith
import com.uniplat.uniplatapi.extensions.withAuthentication
import com.uniplat.uniplatapi.extensions.withValidateSuspend
import com.uniplat.uniplatapi.model.PaginatedResponse
import com.uniplat.uniplatapi.service.UserService
import com.uniplat.uniplatapi.utils.getURL
import org.springframework.core.convert.ConversionService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import javax.validation.Validator

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
    private val conversionService: ConversionService,
    private val validator: Validator
) {

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER')")
    suspend fun getMe(): User = withAuthentication {
        userService.getById(it.id!!)
    }

    @GetMapping
    suspend fun getAll(@PageableDefault pageable: Pageable): PaginatedResponse<UserDTOResponse> = withAuthentication {
        userService.getAll(it.id!!, pageable).convertWith(conversionService)
    }

    @GetMapping("/{id}")
    suspend fun getById(@PathVariable id: UUID): UserDTOResponse = withAuthentication {
        conversionService.convert(userService.getById(id, it.id!!))
    }

    @PostMapping("/register")
    suspend fun create(@RequestBody request: CreateUserRequest, serverHttpRequest: ServerHttpRequest): UserResponse {
        return validator.withValidateSuspend(request) {
            conversionService.convert(userService.create(request, getURL(serverHttpRequest)))
        }
    }

    @PutMapping
    suspend fun update(@RequestBody request: UpdateUserRequest): UserDTOResponse = withAuthentication {
        validator.withValidateSuspend(request) {
            conversionService.convert(userService.update(it.id!!, request))
        }
    }

    @PutMapping("/password")
    suspend fun updatePassword(@RequestBody request: UpdateUserPasswordRequest): UserResponse = withAuthentication {
        validator.withValidateSuspend(request) {
            conversionService.convert(userService.updatePassword(it.id!!, request))
        }
    }

    @DeleteMapping
    suspend fun delete() = withAuthentication {
        userService.delete(it.id!!)
    }
}
