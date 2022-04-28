package com.uniplat.uniplatapi.domain.mapper

import com.uniplat.uniplatapi.domain.dto.response.ActivityParticipantResponse
import com.uniplat.uniplatapi.domain.model.ActivityParticipant
import org.mapstruct.Mapper
import org.springframework.core.convert.converter.Converter

@Mapper
interface ActivityParticipantResponseMapper : Converter<ActivityParticipant, ActivityParticipantResponse>
