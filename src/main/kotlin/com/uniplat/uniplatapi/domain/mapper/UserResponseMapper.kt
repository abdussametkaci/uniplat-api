package com.uniplat.uniplatapi.domain.mapper

import com.uniplat.uniplatapi.component.ContextAwareConverter
import com.uniplat.uniplatapi.domain.dto.response.UserResponse
import com.uniplat.uniplatapi.domain.model.User
import org.mapstruct.Mapper

@Mapper
interface UserResponseMapper : ContextAwareConverter<User, UserResponse>
