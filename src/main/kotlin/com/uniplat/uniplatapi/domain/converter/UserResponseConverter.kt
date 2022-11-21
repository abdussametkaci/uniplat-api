package com.uniplat.uniplatapi.domain.converter

import com.uniplat.uniplatapi.domain.dto.response.UserResponse
import com.uniplat.uniplatapi.domain.model.User
import org.mapstruct.Mapper
import org.springframework.core.convert.converter.Converter

@Mapper
interface UserResponseConverter : Converter<User, UserResponse>
