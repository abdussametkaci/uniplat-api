package com.uniplat.uniplatapi.domain.converter

import com.uniplat.uniplatapi.domain.dto.response.UserFollowResponse
import com.uniplat.uniplatapi.domain.model.UserFollow
import org.mapstruct.Mapper
import org.springframework.core.convert.converter.Converter

@Mapper
interface UserFollowResponseConverter : Converter<UserFollow, UserFollowResponse>
