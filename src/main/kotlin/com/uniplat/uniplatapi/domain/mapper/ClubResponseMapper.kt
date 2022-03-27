package com.uniplat.uniplatapi.domain.mapper

import com.uniplat.uniplatapi.domain.dto.response.ClubResponse
import com.uniplat.uniplatapi.domain.model.Club
import org.mapstruct.Mapper
import org.springframework.core.convert.converter.Converter

@Mapper
interface ClubResponseMapper : Converter<Club, ClubResponse>
