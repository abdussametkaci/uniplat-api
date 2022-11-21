package com.uniplat.uniplatapi.configuration

import com.uniplat.uniplatapi.domain.converter.ActivityParticipantResponseConverter
import com.uniplat.uniplatapi.domain.converter.ClubDTOResponseConverter
import com.uniplat.uniplatapi.domain.converter.ClubResponseConverter
import com.uniplat.uniplatapi.domain.converter.ClubToSearchResponseConverter
import com.uniplat.uniplatapi.domain.converter.PostCommentResponseConverter
import com.uniplat.uniplatapi.domain.converter.PostDTOResponseConverter
import com.uniplat.uniplatapi.domain.converter.PostResponseConverter
import com.uniplat.uniplatapi.domain.converter.PostToSearchResponseConverter
import com.uniplat.uniplatapi.domain.converter.UniversityDTOResponseConverter
import com.uniplat.uniplatapi.domain.converter.UniversityResponseConverter
import com.uniplat.uniplatapi.domain.converter.UniversityToSearchResponseConverter
import com.uniplat.uniplatapi.domain.converter.UserDTOResponseConverter
import com.uniplat.uniplatapi.domain.converter.UserFollowResponseConverter
import com.uniplat.uniplatapi.domain.converter.UserLikedPostResponseConverter
import com.uniplat.uniplatapi.domain.converter.UserResponseConverter
import com.uniplat.uniplatapi.domain.converter.UserToSearchResponseConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ConversionServiceFactoryBean

@Configuration
class ConversionConfiguration {

    @Bean
    fun conversionService(
        activityParticipantResponseConverter: ActivityParticipantResponseConverter,
        clubDTOResponseConverter: ClubDTOResponseConverter,
        clubResponseConverter: ClubResponseConverter,
        clubToSearchResponseConverter: ClubToSearchResponseConverter,
        postCommentResponseConverter: PostCommentResponseConverter,
        postDTOResponseConverter: PostDTOResponseConverter,
        postResponseConverter: PostResponseConverter,
        postToSearchResponseConverter: PostToSearchResponseConverter,
        universityDTOResponseConverter: UniversityDTOResponseConverter,
        universityResponseConverter: UniversityResponseConverter,
        universityToSearchResponseConverter: UniversityToSearchResponseConverter,
        userDTOResponseConverter: UserDTOResponseConverter,
        userFollowResponseConverter: UserFollowResponseConverter,
        userLikedPostResponseConverter: UserLikedPostResponseConverter,
        userResponseConverter: UserResponseConverter,
        userToSearchResponseConverter: UserToSearchResponseConverter
    ): ConversionServiceFactoryBean {
        val converters = setOf(
            activityParticipantResponseConverter,
            clubDTOResponseConverter,
            clubResponseConverter,
            clubToSearchResponseConverter,
            postCommentResponseConverter,
            postDTOResponseConverter,
            postResponseConverter,
            postToSearchResponseConverter,
            universityDTOResponseConverter,
            universityResponseConverter,
            universityToSearchResponseConverter,
            userDTOResponseConverter,
            userFollowResponseConverter,
            userLikedPostResponseConverter,
            userResponseConverter,
            userToSearchResponseConverter
        )

        return ConversionServiceFactoryBean().apply { setConverters(converters) }
    }
}

// NOTE: If you use spring security, you must this configuration
