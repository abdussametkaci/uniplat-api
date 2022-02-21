package com.uniplat.uniplatapi.domain.mapper

import com.uniplat.uniplatapi.component.ContextAwareConverter
import com.uniplat.uniplatapi.domain.dto.response.UniversityResponse
import com.uniplat.uniplatapi.domain.model.University
import org.mapstruct.Mapper

@Mapper
interface UniversityResponseMapper : ContextAwareConverter<University, UniversityResponse>
