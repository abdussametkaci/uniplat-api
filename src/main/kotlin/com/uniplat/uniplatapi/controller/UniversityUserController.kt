package com.uniplat.uniplatapi.controller

import com.uniplat.uniplatapi.domain.dto.request.create.CreateUniversityUserRequest
import com.uniplat.uniplatapi.domain.dto.request.update.UpdateUniversityUserRequest
import com.uniplat.uniplatapi.domain.dto.response.UniversityUserResponse
import com.uniplat.uniplatapi.extensions.convert
import com.uniplat.uniplatapi.extensions.convertWith
import com.uniplat.uniplatapi.model.PaginatedResponse
import com.uniplat.uniplatapi.service.UniversityUserService
import org.springframework.core.convert.ConversionService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/university-users")
class UniversityUserController(
    private val universityUserService: UniversityUserService,
    private val conversionService: ConversionService
) {

    @GetMapping
    suspend fun getAll(@PageableDefault pageable: Pageable): PaginatedResponse<UniversityUserResponse> {
        return universityUserService.getAll(pageable).convertWith(conversionService)
    }

    @GetMapping("/{id}")
    suspend fun getById(@PathVariable id: UUID): UniversityUserResponse {
        return conversionService.convert(universityUserService.getById(id))
    }

    @PostMapping
    suspend fun create(@RequestBody request: CreateUniversityUserRequest): UniversityUserResponse {
        return conversionService.convert(universityUserService.create(request))
    }

    @PatchMapping("/{id}")
    suspend fun update(@PathVariable id: UUID, @RequestBody request: UpdateUniversityUserRequest): UniversityUserResponse {
        return conversionService.convert(universityUserService.update(id, request))
    }

    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id: UUID) {
        universityUserService.delete(id)
    }
}
