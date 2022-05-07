package com.uniplat.uniplatapi.domain.mapper

import com.uniplat.uniplatapi.domain.dto.response.UserDTOResponse
import com.uniplat.uniplatapi.domain.model.UserDTO
import org.mapstruct.Mapper
import org.springframework.core.convert.converter.Converter

@Mapper
interface UserDTOResponseMapper : Converter<UserDTO, UserDTOResponse>
