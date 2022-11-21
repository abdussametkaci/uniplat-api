package com.uniplat.uniplatapi.domain.converter

import com.uniplat.uniplatapi.domain.dto.response.ClubResponse
import com.uniplat.uniplatapi.domain.model.Club
import org.mapstruct.Mapper
import org.springframework.core.convert.converter.Converter

@Mapper
interface ClubResponseConverter : Converter<Club, ClubResponse>
