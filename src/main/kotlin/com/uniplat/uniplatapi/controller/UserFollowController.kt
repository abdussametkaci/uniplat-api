package com.uniplat.uniplatapi.controller

import com.uniplat.uniplatapi.domain.dto.request.create.CreateUserFollowRequest
import com.uniplat.uniplatapi.domain.dto.response.UserFollowResponse
import com.uniplat.uniplatapi.extensions.convert
import com.uniplat.uniplatapi.extensions.convertWith
import com.uniplat.uniplatapi.model.PaginatedResponse
import com.uniplat.uniplatapi.service.UserFollowService
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
@RequestMapping("/user-follows")
class UserFollowController(
    private val userFollowService: UserFollowService,
    private val conversionService: ConversionService
) {

    @GetMapping
    suspend fun getAll(
        @RequestParam userId: UUID?,
        @RequestParam contactId: UUID?,
        @PageableDefault pageable: Pageable
    ): PaginatedResponse<UserFollowResponse> {
        return userFollowService.getAll(userId, contactId, pageable).convertWith(conversionService)
    }

    @PostMapping
    suspend fun create(@RequestBody request: CreateUserFollowRequest): UserFollowResponse {
        return conversionService.convert(userFollowService.create(request))
    }

    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id: UUID) {
        userFollowService.delete(id)
    }
}
