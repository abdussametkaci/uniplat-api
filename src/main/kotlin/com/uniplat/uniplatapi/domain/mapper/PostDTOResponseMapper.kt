package com.uniplat.uniplatapi.domain.mapper

import com.uniplat.uniplatapi.domain.dto.response.PostDTOResponse
import com.uniplat.uniplatapi.domain.model.PostDTO
import org.mapstruct.Mapper
import org.springframework.core.convert.converter.Converter

@Mapper
interface PostDTOResponseMapper : Converter<PostDTO, PostDTOResponse>
