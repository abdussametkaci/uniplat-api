package com.uniplat.uniplatapi.model

class PaginatedResponse<T>(
    private val number: Int,
    private val size: Int,
    private val totalElements: Long,
    private val totalPages: Int,
    val content: List<T>
) {
    val page = PageResponse(number, size, totalElements, totalPages)
}

data class PageResponse(
    val number: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int
)
