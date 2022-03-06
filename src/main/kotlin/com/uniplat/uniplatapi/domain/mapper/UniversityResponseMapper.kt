package com.uniplat.uniplatapi.domain.mapper

import org.springframework.core.convert.converter.Converter
import com.uniplat.uniplatapi.domain.dto.response.UniversityResponse
import com.uniplat.uniplatapi.domain.model.University
import org.mapstruct.Mapper

@Mapper
interface UniversityResponseMapper : Converter<University, UniversityResponse>
