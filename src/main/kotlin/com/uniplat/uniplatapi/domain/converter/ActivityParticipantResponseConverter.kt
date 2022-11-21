package com.uniplat.uniplatapi.domain.converter

import com.uniplat.uniplatapi.domain.dto.response.ActivityParticipantResponse
import com.uniplat.uniplatapi.domain.model.ActivityParticipant
import org.mapstruct.Mapper
import org.springframework.core.convert.converter.Converter

@Mapper
interface ActivityParticipantResponseConverter : Converter<ActivityParticipant, ActivityParticipantResponse>
