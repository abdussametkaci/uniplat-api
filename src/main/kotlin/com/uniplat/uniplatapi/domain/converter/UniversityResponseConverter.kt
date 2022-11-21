package com.uniplat.uniplatapi.domain.converter

import com.uniplat.uniplatapi.domain.dto.response.UniversityResponse
import com.uniplat.uniplatapi.domain.model.University
import org.mapstruct.Mapper
import org.springframework.core.convert.converter.Converter

@Mapper
interface UniversityResponseConverter : Converter<University, UniversityResponse>
