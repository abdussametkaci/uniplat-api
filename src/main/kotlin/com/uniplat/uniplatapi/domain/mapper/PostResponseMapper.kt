package com.uniplat.uniplatapi.domain.mapper

import com.uniplat.uniplatapi.domain.dto.response.PostResponse
import com.uniplat.uniplatapi.domain.model.Post
import org.mapstruct.Mapper
import org.springframework.core.convert.converter.Converter

@Mapper
interface PostResponseMapper : Converter<Post, PostResponse>
