package com.uniplat.uniplatapi.domain.mapper

import com.uniplat.uniplatapi.domain.dto.response.UserClubResponse
import com.uniplat.uniplatapi.domain.model.UserClub
import org.mapstruct.Mapper
import org.springframework.core.convert.converter.Converter

@Mapper
interface UserClubResponseMapper : Converter<UserClub, UserClubResponse>
