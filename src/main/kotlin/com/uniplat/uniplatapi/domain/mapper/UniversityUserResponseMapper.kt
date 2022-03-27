package com.uniplat.uniplatapi.domain.mapper

import com.uniplat.uniplatapi.domain.dto.response.UniversityUserResponse
import com.uniplat.uniplatapi.domain.model.UniversityUser
import org.mapstruct.Mapper
import org.springframework.core.convert.converter.Converter

@Mapper
interface UniversityUserResponseMapper : Converter<UniversityUser, UniversityUserResponse>
