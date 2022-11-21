package com.uniplat.uniplatapi.controller

import com.uniplat.uniplatapi.domain.dto.request.create.CreateUniversityRequest
import com.uniplat.uniplatapi.domain.dto.request.update.UpdateUniversityRequest
import com.uniplat.uniplatapi.domain.dto.response.UniversityDTOResponse
import com.uniplat.uniplatapi.domain.dto.response.UniversityResponse
import com.uniplat.uniplatapi.extensions.convert
import com.uniplat.uniplatapi.extensions.convertWith
import com.uniplat.uniplatapi.extensions.withAuthentication
import com.uniplat.uniplatapi.extensions.withAuthenticationOrNull
import com.uniplat.uniplatapi.extensions.withValidateSuspend
import com.uniplat.uniplatapi.model.PaginatedResponse
import com.uniplat.uniplatapi.service.UniversityService
import org.springframework.core.convert.ConversionService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import javax.validation.Validator

@RestController
@RequestMapping("/universities")
class UniversityController(
    private val universityService: UniversityService,
    private val conversionService: ConversionService,
    private val validator: Validator
) {

    @GetMapping
    suspend fun getAll(
        @RequestParam adminId: UUID?,
        @PageableDefault pageable: Pageable
    ): PaginatedResponse<UniversityDTOResponse> = withAuthenticationOrNull {
        universityService.getAll(it?.id!!, adminId, pageable).convertWith(conversionService)
    }

    @GetMapping("/{id}")
    suspend fun getById(@PathVariable id: UUID): UniversityDTOResponse = withAuthentication {
        conversionService.convert(universityService.getById(id, it.id!!))
    }

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    suspend fun create(@RequestBody request: CreateUniversityRequest): UniversityResponse = withAuthentication {
        validator.withValidateSuspend(request) {
            conversionService.convert(universityService.create(request, it.id!!))
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    suspend fun update(@PathVariable id: UUID, @RequestBody request: UpdateUniversityRequest): UniversityDTOResponse = withAuthentication {
        validator.withValidateSuspend(request) {
            conversionService.convert(universityService.update(id, request, it.id!!))
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    suspend fun delete(@PathVariable id: UUID) {
        universityService.delete(id)
    }
}
