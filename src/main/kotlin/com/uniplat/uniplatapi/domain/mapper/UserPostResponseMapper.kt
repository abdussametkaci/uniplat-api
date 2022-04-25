package com.uniplat.uniplatapi.domain.mapper

import com.uniplat.uniplatapi.domain.dto.response.UserPostResponse
import org.mapstruct.Mapper
import org.springframework.core.convert.converter.Converter

@Mapper
interface UserPostResponseMapper : Converter<UserPost, UserPostResponse>
