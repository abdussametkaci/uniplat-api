package com.uniplat.uniplatapi.controller

import com.uniplat.uniplatapi.domain.dto.request.create.CreateActivityParticipantRequest
import com.uniplat.uniplatapi.domain.dto.response.ActivityParticipantResponse
import com.uniplat.uniplatapi.extensions.convert
import com.uniplat.uniplatapi.extensions.convertWith
import com.uniplat.uniplatapi.extensions.withAuthentication
import com.uniplat.uniplatapi.model.PaginatedResponse
import com.uniplat.uniplatapi.service.ActivityParticipantService
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
@RequestMapping("/activity-participants")
class ActivityParticipantController(
    private val activityParticipantService: ActivityParticipantService,
    private val conversionService: ConversionService
) {

    @GetMapping
    suspend fun getAll(
        @RequestParam userId: UUID?,
        @RequestParam postId: UUID?,
        @PageableDefault pageable: Pageable
    ): PaginatedResponse<ActivityParticipantResponse> {
        return activityParticipantService.getAll(userId, postId, pageable).convertWith(conversionService)
    }

    @PostMapping
    suspend fun create(@RequestBody request: CreateActivityParticipantRequest): ActivityParticipantResponse = withAuthentication {
        conversionService.convert(activityParticipantService.create(request, it.id!!))
    }

    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id: UUID) {
        activityParticipantService.delete(id)
    }
}
