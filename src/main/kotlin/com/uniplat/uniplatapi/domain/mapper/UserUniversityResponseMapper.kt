package com.uniplat.uniplatapi.domain.mapper

import com.uniplat.uniplatapi.domain.dto.response.UniversityUserResponse
import com.uniplat.uniplatapi.domain.model.UserUniversity
import org.mapstruct.Mapper
import org.springframework.core.convert.converter.Converter

@Mapper
interface UserUniversityResponseMapper : Converter<UserUniversity, UniversityUserResponse>
