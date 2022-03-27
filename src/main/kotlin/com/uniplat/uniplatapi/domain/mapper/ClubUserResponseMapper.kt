package com.uniplat.uniplatapi.domain.mapper

import com.uniplat.uniplatapi.domain.dto.response.ClubUserResponse
import com.uniplat.uniplatapi.domain.model.ClubUser
import org.mapstruct.Mapper
import org.springframework.core.convert.converter.Converter

@Mapper
interface ClubUserResponseMapper : Converter<ClubUser, ClubUserResponse>
