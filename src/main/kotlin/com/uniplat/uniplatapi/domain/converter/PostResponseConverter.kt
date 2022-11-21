package com.uniplat.uniplatapi.domain.converter

import com.uniplat.uniplatapi.domain.dto.response.PostResponse
import com.uniplat.uniplatapi.domain.model.Post
import org.mapstruct.Mapper
import org.springframework.core.convert.converter.Converter

@Mapper
interface PostResponseConverter : Converter<Post, PostResponse>
