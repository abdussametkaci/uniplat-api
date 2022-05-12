package com.uniplat.uniplatapi.controller

import com.uniplat.uniplatapi.domain.dto.response.SearchResponse
import com.uniplat.uniplatapi.domain.enums.SearchType
import com.uniplat.uniplatapi.model.PaginatedResponse
import com.uniplat.uniplatapi.service.SearchService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/searches")
class SearchController(private val searchService: SearchService) {

    @GetMapping
    suspend fun getAll(
        @RequestParam filters: List<SearchType>,
        @RequestParam text: String,
        @PageableDefault pageable: Pageable
    ): PaginatedResponse<SearchResponse> {
        return searchService.search(filters, text, pageable)
    }
}
