package com.uniplat.uniplatapi.domain.mapper

import com.uniplat.uniplatapi.domain.dto.response.UserResponse
import com.uniplat.uniplatapi.domain.model.User
import org.mapstruct.Mapper
import org.springframework.core.convert.converter.Converter

@Mapper
interface UserResponseMapper : Converter<User, UserResponse>
