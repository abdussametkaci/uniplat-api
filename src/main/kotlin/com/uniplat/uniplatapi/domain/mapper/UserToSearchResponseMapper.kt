package com.uniplat.uniplatapi.domain.mapper

import com.uniplat.uniplatapi.domain.dto.response.SearchResponse
import com.uniplat.uniplatapi.domain.enums.SearchType
import com.uniplat.uniplatapi.domain.model.User
import org.mapstruct.AfterMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.Mappings
import org.springframework.core.convert.converter.Converter

@Mapper
interface UserToSearchResponseMapper : Converter<User, SearchResponse> {

    @Mappings(
        Mapping(target = "name", ignore = true),
        Mapping(target = "searchType", ignore = true)
    )
    override fun convert(source: User): SearchResponse

    @JvmDefault
    @AfterMapping
    fun mapName(@MappingTarget target: SearchResponse, source: User) {
        target.name = source.name + " " + source.surname
        target.searchType = SearchType.USER
    }
}
