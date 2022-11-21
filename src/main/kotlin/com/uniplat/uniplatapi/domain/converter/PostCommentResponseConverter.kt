package com.uniplat.uniplatapi.domain.converter

import com.uniplat.uniplatapi.domain.dto.response.PostCommentResponse
import com.uniplat.uniplatapi.domain.model.PostComment
import org.mapstruct.Mapper
import org.springframework.core.convert.converter.Converter

@Mapper
interface PostCommentResponseConverter : Converter<PostComment, PostCommentResponse>
