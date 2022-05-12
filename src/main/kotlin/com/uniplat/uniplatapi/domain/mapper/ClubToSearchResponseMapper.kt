package com.uniplat.uniplatapi.domain.mapper

import com.uniplat.uniplatapi.domain.dto.response.SearchResponse
import com.uniplat.uniplatapi.domain.enums.SearchType
import com.uniplat.uniplatapi.domain.model.Club
import org.mapstruct.AfterMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.Mappings
import org.springframework.core.convert.converter.Converter

@Mapper
interface ClubToSearchResponseMapper : Converter<Club, SearchResponse> {

    @Mappings(
        Mapping(target = "searchType", ignore = true)
    )
    override fun convert(source: Club): SearchResponse

    @JvmDefault
    @AfterMapping
    fun mapName(@MappingTarget target: SearchResponse, source: Club) {
        target.searchType = SearchType.CLUB
    }
}
