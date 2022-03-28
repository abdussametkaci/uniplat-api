package com.uniplat.uniplatapi.controller

import com.uniplat.uniplatapi.domain.dto.request.create.CreateUniversityClubRequest
import com.uniplat.uniplatapi.domain.dto.request.update.UpdateUniversityClubRequest
import com.uniplat.uniplatapi.domain.dto.response.UniversityClubResponse
import com.uniplat.uniplatapi.extensions.convert
import com.uniplat.uniplatapi.extensions.convertWith
import com.uniplat.uniplatapi.model.PaginatedResponse
import com.uniplat.uniplatapi.service.UniversityClubService
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
@RequestMapping("/university-clubs")
class UniversityClubController(
    private val universityClubService: UniversityClubService,
    private val conversionService: ConversionService
) {

    @GetMapping
    suspend fun getAll(@PageableDefault pageable: Pageable): PaginatedResponse<UniversityClubResponse> {
        return universityClubService.getAll(pageable).convertWith(conversionService)
    }

    @GetMapping("/{id}")
    suspend fun getById(@PathVariable id: UUID): UniversityClubResponse {
        return conversionService.convert(universityClubService.getById(id))
    }

    @PostMapping
    suspend fun create(@RequestBody request: CreateUniversityClubRequest): UniversityClubResponse {
        return conversionService.convert(universityClubService.create(request))
    }

    @PatchMapping("/{id}")
    suspend fun update(@PathVariable id: UUID, @RequestBody request: UpdateUniversityClubRequest): UniversityClubResponse {
        return conversionService.convert(universityClubService.update(id, request))
    }

    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id: UUID) {
        universityClubService.delete(id)
    }
}
