package com.uniplat.uniplatapi.domain.converter

import com.uniplat.uniplatapi.domain.dto.response.ClubDTOResponse
import com.uniplat.uniplatapi.domain.model.ClubDTO
import org.mapstruct.Mapper
import org.springframework.core.convert.converter.Converter

@Mapper
interface ClubDTOResponseConverter : Converter<ClubDTO, ClubDTOResponse>
