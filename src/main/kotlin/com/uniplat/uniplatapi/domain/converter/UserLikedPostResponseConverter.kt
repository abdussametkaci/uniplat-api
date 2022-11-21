package com.uniplat.uniplatapi.domain.converter

import com.uniplat.uniplatapi.domain.dto.response.UserLikedPostResponse
import com.uniplat.uniplatapi.domain.model.UserLikedPost
import org.mapstruct.Mapper
import org.springframework.core.convert.converter.Converter

@Mapper
interface UserLikedPostResponseConverter : Converter<UserLikedPost, UserLikedPostResponse>
