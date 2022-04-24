package com.uniplat.uniplatapi.controller

import com.uniplat.uniplatapi.domain.dto.request.create.CreateUniversityUserRequest
import com.uniplat.uniplatapi.domain.dto.response.UserUniversityResponse
import com.uniplat.uniplatapi.extensions.convert
import com.uniplat.uniplatapi.extensions.convertWith
import com.uniplat.uniplatapi.model.PaginatedResponse
import com.uniplat.uniplatapi.service.UserUniversityService
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
@RequestMapping("/user-universities")
class UserUniversityController(
    private val userUniversityService: UserUniversityService,
    private val conversionService: ConversionService
) {

    @GetMapping
    suspend fun getAll(@PageableDefault pageable: Pageable): PaginatedResponse<UserUniversityResponse> {
        return userUniversityService.getAll(pageable).convertWith(conversionService)
    }

    @PostMapping
    suspend fun create(@RequestBody request: CreateUniversityUserRequest): UserUniversityResponse {
        return conversionService.convert(userUniversityService.create(request))
    }

    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id: UUID) {
        userUniversityService.delete(id)
    }
}
