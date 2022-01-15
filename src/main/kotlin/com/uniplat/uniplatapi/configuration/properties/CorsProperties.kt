package com.uniplat.uniplatapi.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("cors")
@ConstructorBinding
data class CorsProperties(
    val allowedOrigins: List<String>
)
