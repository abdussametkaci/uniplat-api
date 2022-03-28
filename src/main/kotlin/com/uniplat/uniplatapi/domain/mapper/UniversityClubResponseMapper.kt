package com.uniplat.uniplatapi.domain.mapper

import com.uniplat.uniplatapi.domain.dto.response.UniversityClubResponse
import com.uniplat.uniplatapi.domain.model.UniversityClub
import org.mapstruct.Mapper
import org.springframework.core.convert.converter.Converter

@Mapper
interface UniversityClubResponseMapper : Converter<UniversityClub, UniversityClubResponse>
