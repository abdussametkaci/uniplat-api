package com.uniplat.uniplatapi.controller

import com.uniplat.uniplatapi.domain.dto.response.PostResponse
import com.uniplat.uniplatapi.extensions.convertWith
import com.uniplat.uniplatapi.model.PaginatedResponse
import com.uniplat.uniplatapi.service.PostService
import org.springframework.core.convert.ConversionService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/post-flows")
class PostFlowController(
    private val postService: PostService,
    private val conversionService: ConversionService
) {

    @GetMapping
    suspend fun postFlowByUserId(
        @RequestParam userId: UUID,
        @PageableDefault pageable: Pageable
    ): PaginatedResponse<PostResponse> {
        return postService.postFlowByUserId(userId, pageable).convertWith(conversionService)
    }
}
