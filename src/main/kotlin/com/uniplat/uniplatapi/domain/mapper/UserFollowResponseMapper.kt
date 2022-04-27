package com.uniplat.uniplatapi.domain.mapper

import com.uniplat.uniplatapi.domain.dto.response.UserFollowResponse
import com.uniplat.uniplatapi.domain.model.UserFollow
import org.mapstruct.Mapper
import org.springframework.core.convert.converter.Converter

@Mapper
interface UserFollowResponseMapper : Converter<UserFollow, UserFollowResponse>
