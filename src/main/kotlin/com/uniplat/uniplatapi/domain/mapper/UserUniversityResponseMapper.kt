package com.uniplat.uniplatapi.domain.mapper

import com.uniplat.uniplatapi.domain.dto.response.UserUniversityResponse
import com.uniplat.uniplatapi.domain.model.UserUniversity
import org.mapstruct.Mapper
import org.springframework.core.convert.converter.Converter

@Mapper
interface UserUniversityResponseMapper : Converter<UserUniversity, UserUniversityResponse>
