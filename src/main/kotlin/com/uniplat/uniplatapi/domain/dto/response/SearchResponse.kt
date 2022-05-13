package com.uniplat.uniplatapi.domain.dto.response

import com.uniplat.uniplatapi.domain.enums.SearchType
import java.util.UUID

data class SearchResponse(
    val id: UUID,
    val description: String?,
    val profileImgId: UUID?
) {
    lateinit var name: String
    lateinit var searchType: SearchType
}
