package com.uniplat.uniplatapi.domain.mapper

import com.uniplat.uniplatapi.domain.dto.response.UserPostResponse
import com.uniplat.uniplatapi.domain.model.UserPost
import org.mapstruct.Mapper
import org.springframework.core.convert.converter.Converter

@Mapper
interface UserPostResponseMapper : Converter<UserPost, UserPostResponse>
