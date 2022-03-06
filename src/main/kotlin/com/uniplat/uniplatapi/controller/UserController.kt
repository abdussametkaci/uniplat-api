package com.uniplat.uniplatapi.controller

import com.uniplat.uniplatapi.domain.dto.request.CreateUserRequest
import com.uniplat.uniplatapi.domain.dto.response.UserResponse
import com.uniplat.uniplatapi.extensions.convert
import com.uniplat.uniplatapi.extensions.convertWith
import com.uniplat.uniplatapi.model.PaginatedResponse
import com.uniplat.uniplatapi.service.UserService
import org.springframework.core.convert.ConversionService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
    private val conversionService: ConversionService
) {

    @GetMapping
    suspend fun getUsers(@PageableDefault pageable: Pageable): PaginatedResponse<UserResponse> {
        return userService.getUsers(pageable).convertWith(conversionService)
    }

    @GetMapping("/{id}")
    suspend fun getUser(@PathVariable id: UUID): UserResponse {
        return conversionService.convert(userService.getById(id))
    }

    @PostMapping
    suspend fun createUser(@RequestBody createUserRequest: CreateUserRequest): UserResponse {
        return conversionService.convert(userService.create(createUserRequest))
    }
}
