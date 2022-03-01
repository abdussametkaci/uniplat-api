package com.uniplat.uniplatapi.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.time.Duration

@ConstructorBinding
@ConfigurationProperties(prefix = "api.file-api")
data class FileClientProperties(
    val baseUrl: String,
    val connectionTimeout: Duration,
    val readTimeout: Duration
)
