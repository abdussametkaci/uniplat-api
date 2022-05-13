package com.uniplat.uniplatapi.domain.mapper

import com.uniplat.uniplatapi.domain.dto.response.SearchResponse
import com.uniplat.uniplatapi.domain.enums.PostType
import com.uniplat.uniplatapi.domain.enums.SearchType
import com.uniplat.uniplatapi.domain.model.Post
import org.mapstruct.AfterMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.Mappings
import org.springframework.core.convert.converter.Converter

@Mapper
interface PostToSearchResponseMapper : Converter<Post, SearchResponse> {

    @Mappings(
        Mapping(target = "searchType", ignore = true),
        Mapping(target = "profileImgId", ignore = true)
    )
    override fun convert(source: Post): SearchResponse

    @JvmDefault
    @AfterMapping
    fun mapName(@MappingTarget target: SearchResponse, source: Post) {
        target.name = if (source.postType == PostType.ACTIVITY) source.activityTitle!! else ""
        target.searchType = SearchType.POST
    }
}
