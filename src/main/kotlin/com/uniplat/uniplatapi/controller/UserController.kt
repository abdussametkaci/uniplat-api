package com.uniplat.uniplatapi.controller

import com.uniplat.uniplatapi.domain.dto.request.create.CreateUserRequest
import com.uniplat.uniplatapi.domain.dto.request.update.UpdateUserPasswordRequest
import com.uniplat.uniplatapi.domain.dto.request.update.UpdateUserRequest
import com.uniplat.uniplatapi.domain.dto.response.UserDTOResponse
import com.uniplat.uniplatapi.domain.dto.response.UserResponse
import com.uniplat.uniplatapi.extensions.convert
import com.uniplat.uniplatapi.extensions.convertWith
import com.uniplat.uniplatapi.extensions.withUserId
import com.uniplat.uniplatapi.extensions.withValidateSuspend
import com.uniplat.uniplatapi.model.PaginatedResponse
import com.uniplat.uniplatapi.service.UserService
import com.uniplat.uniplatapi.utils.getURL
import org.springframework.core.convert.ConversionService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.server.reactive.ServerHttpRequest
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

    @GetMapping
    suspend fun getAll(@PageableDefault pageable: Pageable): PaginatedResponse<UserDTOResponse> {
        return withUserId { userId ->
            userService.getAll(userId, pageable).convertWith(conversionService)
        }
    }

    @GetMapping("/{id}")
    suspend fun getById(@PathVariable id: UUID): UserDTOResponse {
        return withUserId { userId ->
            conversionService.convert(userService.getById(id, userId))
        }
    }

    @PostMapping
    suspend fun create(@RequestBody request: CreateUserRequest, serverHttpRequest: ServerHttpRequest): UserResponse {
        return validator.withValidateSuspend(request) {
            conversionService.convert(userService.create(request, getURL(serverHttpRequest)))
        }
    }

    @PutMapping("/{id}")
    suspend fun update(@PathVariable id: UUID, @RequestBody request: UpdateUserRequest): UserDTOResponse {
        return withUserId { userId ->
            validator.withValidateSuspend(request) {
                conversionService.convert(userService.update(id, request, userId))
            }
        }
    }

    @PutMapping("/{id}/password")
    suspend fun updatePassword(@PathVariable id: UUID, @RequestBody request: UpdateUserPasswordRequest): UserResponse {
        return validator.withValidateSuspend(request) {
            conversionService.convert(userService.updatePassword(id, request))
        }
    }

    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id: UUID) {
        userService.delete(id)
    }
}
