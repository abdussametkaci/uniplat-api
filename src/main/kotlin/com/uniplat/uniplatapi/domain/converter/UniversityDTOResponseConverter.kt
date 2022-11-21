package com.uniplat.uniplatapi.domain.converter

import com.uniplat.uniplatapi.domain.dto.response.UniversityDTOResponse
import com.uniplat.uniplatapi.domain.model.UniversityDTO
import org.mapstruct.Mapper
import org.springframework.core.convert.converter.Converter

@Mapper
interface UniversityDTOResponseConverter : Converter<UniversityDTO, UniversityDTOResponse>
