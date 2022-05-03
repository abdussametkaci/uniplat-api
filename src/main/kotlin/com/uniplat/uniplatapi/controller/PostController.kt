package com.uniplat.uniplatapi.controller

import com.uniplat.uniplatapi.domain.dto.request.create.CreatePostRequest
import com.uniplat.uniplatapi.domain.dto.request.update.UpdatePostRequest
import com.uniplat.uniplatapi.domain.dto.response.PostResponse
import com.uniplat.uniplatapi.domain.enums.OwnerType
import com.uniplat.uniplatapi.extensions.convert
import com.uniplat.uniplatapi.extensions.convertWith
import com.uniplat.uniplatapi.extensions.withValidateSuspend
import com.uniplat.uniplatapi.model.PaginatedResponse
import com.uniplat.uniplatapi.service.PostService
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
import javax.validation.Validator

@RestController
@RequestMapping("/posts")
class PostController(
    private val postService: PostService,
    private val conversionService: ConversionService,
    private val validator: Validator
) {

    @GetMapping
    suspend fun getAll(
        @RequestParam ownerId: UUID?,
        @RequestParam ownerType: OwnerType?,
        @PageableDefault pageable: Pageable
    ): PaginatedResponse<PostResponse> {
        return postService.getAll(ownerId, ownerType, pageable).convertWith(conversionService)
    }

    @GetMapping("/{id}")
    suspend fun getById(@PathVariable id: UUID): PostResponse {
        return conversionService.convert(postService.getById(id))
    }

    @PostMapping
    suspend fun create(@RequestBody request: CreatePostRequest): PostResponse {
        return validator.withValidateSuspend(request) {
            conversionService.convert(postService.create(request))
        }
    }

    @PutMapping("/{id}")
    suspend fun update(@PathVariable id: UUID, @RequestBody request: UpdatePostRequest): PostResponse {
        return validator.withValidateSuspend(request) {
            conversionService.convert(postService.update(id, request))
        }
    }

    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id: UUID) {
        postService.delete(id)
    }
}
