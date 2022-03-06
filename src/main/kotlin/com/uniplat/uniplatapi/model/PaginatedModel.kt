package com.uniplat.uniplatapi.model

import kotlinx.coroutines.flow.Flow
import kotlin.math.ceil

data class PaginatedModel<T>(
    val content: Flow<T>,
    val number: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int = ceil(totalElements.toDouble() / size).toInt()
)
