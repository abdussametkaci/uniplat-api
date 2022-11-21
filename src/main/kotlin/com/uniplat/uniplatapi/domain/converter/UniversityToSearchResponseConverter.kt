package com.uniplat.uniplatapi.domain.converter

import com.uniplat.uniplatapi.domain.dto.response.SearchResponse
import com.uniplat.uniplatapi.domain.enums.SearchType
import com.uniplat.uniplatapi.domain.model.University
import org.mapstruct.AfterMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.Mappings
import org.springframework.core.convert.converter.Converter

@Mapper
interface UniversityToSearchResponseConverter : Converter<University, SearchResponse> {

    @Mappings(
        Mapping(target = "searchType", ignore = true)
    )
    override fun convert(source: University): SearchResponse

    @JvmDefault
    @AfterMapping
    fun mapName(@MappingTarget target: SearchResponse, source: University) {
        target.searchType = SearchType.UNIVERSITY
    }
}
