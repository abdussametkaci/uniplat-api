package com.uniplat.uniplatapi.controller

import com.uniplat.uniplatapi.domain.dto.request.create.CreateClubRequest
import com.uniplat.uniplatapi.domain.dto.request.update.UpdateClubRequest
import com.uniplat.uniplatapi.domain.dto.response.ClubDTOResponse
import com.uniplat.uniplatapi.domain.dto.response.ClubResponse
import com.uniplat.uniplatapi.extensions.convert
import com.uniplat.uniplatapi.extensions.convertWith
import com.uniplat.uniplatapi.extensions.withAuthentication
import com.uniplat.uniplatapi.extensions.withValidateSuspend
import com.uniplat.uniplatapi.model.PaginatedResponse
import com.uniplat.uniplatapi.service.ClubService
import jakarta.validation.Validator
import org.springframework.core.convert.ConversionService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
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

@RestController
@RequestMapping("/clubs")
class ClubController(
    private val clubService: ClubService,
    private val conversionService: ConversionService,
    private val validator: Validator
) {

    @GetMapping
    suspend fun getAll(
        @RequestParam universityId: UUID?,
        @RequestParam adminId: UUID?,
        @PageableDefault pageable: Pageable
    ): PaginatedResponse<ClubDTOResponse> = withAuthentication {
        clubService.getAll(it.id!!, universityId, adminId, pageable).convertWith(conversionService)
    }

    @GetMapping("/{id}")
    suspend fun getById(@PathVariable id: UUID): ClubDTOResponse = withAuthentication {
        conversionService.convert(clubService.getById(id, it.id!!))
    }

    @PostMapping
    suspend fun create(@RequestBody request: CreateClubRequest): ClubResponse = withAuthentication {
        validator.withValidateSuspend(request) {
            conversionService.convert(clubService.create(request, it.id!!))
        }
    }

    @PutMapping("/{id}")
    suspend fun update(@PathVariable id: UUID, @RequestBody request: UpdateClubRequest): ClubDTOResponse = withAuthentication {
        validator.withValidateSuspend(request) {
            conversionService.convert(clubService.update(id, request, it.id!!))
        }
    }

    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id: UUID) {
        clubService.delete(id)
    }
}
