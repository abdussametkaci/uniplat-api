package com.uniplat.uniplatapi.controller

import com.uniplat.uniplatapi.domain.dto.request.create.CreateUserClubRequest
import com.uniplat.uniplatapi.domain.dto.response.UserClubResponse
import com.uniplat.uniplatapi.extensions.convert
import com.uniplat.uniplatapi.extensions.convertWith
import com.uniplat.uniplatapi.model.PaginatedResponse
import com.uniplat.uniplatapi.service.UserClubService
import org.springframework.core.convert.ConversionService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/user-clubs")
class UserClubController(
    private val userClubService: UserClubService,
    private val conversionService: ConversionService
) {

    @GetMapping
    suspend fun getAll(
        @RequestParam userId: UUID?,
        @RequestParam clubId: UUID?,
        @PageableDefault pageable: Pageable
    ): PaginatedResponse<UserClubResponse> {
        return userClubService.getAll(pageable).convertWith(conversionService)
    }

    @PostMapping
    suspend fun create(@RequestBody request: CreateUserClubRequest): UserClubResponse {
        return conversionService.convert(userClubService.create(request))
    }

    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id: UUID) {
        userClubService.delete(id)
    }
}
