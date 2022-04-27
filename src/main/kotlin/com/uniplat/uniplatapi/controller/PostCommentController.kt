package com.uniplat.uniplatapi.controller

import com.uniplat.uniplatapi.domain.dto.request.create.CreatePostCommentRequest
import com.uniplat.uniplatapi.domain.dto.response.PostCommentResponse
import com.uniplat.uniplatapi.extensions.convert
import com.uniplat.uniplatapi.extensions.convertWith
import com.uniplat.uniplatapi.extensions.withValidateSuspend
import com.uniplat.uniplatapi.model.PaginatedResponse
import com.uniplat.uniplatapi.service.PostCommentService
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
import javax.validation.Validator

@RestController
@RequestMapping("/post-comments")
class PostCommentController(
    private val postCommentService: PostCommentService,
    private val conversionService: ConversionService,
    private val validator: Validator
) {

    @GetMapping
    suspend fun getAll(
        @RequestParam userId: UUID?,
        @RequestParam postId: UUID?,
        @PageableDefault pageable: Pageable
    ): PaginatedResponse<PostCommentResponse> {
        return postCommentService.getAll(userId, postId, pageable).convertWith(conversionService)
    }

    @PostMapping
    suspend fun create(@RequestBody request: CreatePostCommentRequest): PostCommentResponse {
        return validator.withValidateSuspend(request) {
            conversionService.convert(postCommentService.create(request))
        }
    }

    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id: UUID) {
        postCommentService.delete(id)
    }
}
