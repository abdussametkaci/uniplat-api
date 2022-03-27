package com.uniplat.uniplatapi.controller

import com.uniplat.uniplatapi.domain.dto.request.create.CreateClubUserRequest
import com.uniplat.uniplatapi.domain.dto.request.update.UpdateClubUserRequest
import com.uniplat.uniplatapi.domain.dto.response.ClubUserResponse
import com.uniplat.uniplatapi.extensions.convert
import com.uniplat.uniplatapi.extensions.convertWith
import com.uniplat.uniplatapi.extensions.withValidateSuspend
import com.uniplat.uniplatapi.model.PaginatedResponse
import com.uniplat.uniplatapi.service.ClubUserService
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
import javax.validation.Validator

@RestController
@RequestMapping("/club-users")
class ClubUserController(
    private val clubUserService: ClubUserService,
    private val conversionService: ConversionService,
    private val validator: Validator
) {

    @GetMapping
    suspend fun getAll(@PageableDefault pageable: Pageable): PaginatedResponse<ClubUserResponse> {
        return clubUserService.getAll(pageable).convertWith(conversionService)
    }

    @GetMapping("/{id}")
    suspend fun getById(@PathVariable id: UUID): ClubUserResponse {
        return conversionService.convert(clubUserService.getById(id))
    }

    @PostMapping
    suspend fun create(@RequestBody request: CreateClubUserRequest): ClubUserResponse {
        return validator.withValidateSuspend(request) {
            conversionService.convert(clubUserService.create(request))
        }
    }

    @PatchMapping("/{id}")
    suspend fun update(@PathVariable id: UUID, @RequestBody request: UpdateClubUserRequest): ClubUserResponse {
        return validator.withValidateSuspend(request) {
            conversionService.convert(clubUserService.update(id, request))
        }
    }

    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id: UUID) {
        clubUserService.delete(id)
    }
}
