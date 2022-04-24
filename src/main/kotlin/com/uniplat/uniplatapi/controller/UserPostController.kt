package com.uniplat.uniplatapi.controller

import com.uniplat.uniplatapi.domain.dto.request.create.CreateUserPostRequest
import com.uniplat.uniplatapi.domain.dto.response.UserPostResponse
import com.uniplat.uniplatapi.extensions.convert
import com.uniplat.uniplatapi.extensions.convertWith
import com.uniplat.uniplatapi.model.PaginatedResponse
import com.uniplat.uniplatapi.service.UserPostService
import org.springframework.core.convert.ConversionService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/user-posts")
class UserPostController(
    private val userPostService: UserPostService,
    private val conversionService: ConversionService
) {

    @GetMapping
    suspend fun getAll(@PageableDefault pageable: Pageable): PaginatedResponse<UserPostResponse> {
        return userPostService.getAll(pageable).convertWith(conversionService)
    }

    @GetMapping("/{id}")
    suspend fun getById(@PathVariable id: UUID): UserPostResponse {
        return conversionService.convert(userPostService.getById(id))
    }

    @PostMapping
    suspend fun create(@RequestBody request: CreateUserPostRequest): UserPostResponse {
        return conversionService.convert(userPostService.create(request))
    }

    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id: UUID) {
        userPostService.delete(id)
    }
}
