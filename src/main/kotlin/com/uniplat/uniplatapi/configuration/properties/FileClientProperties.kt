package com.uniplat.uniplatapi.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "api.file-api")
data class FileClientProperties(
    val baseUrl: String,
    val connectionTimeout: Duration,
    val readTimeout: Duration,
    val memorySizeByMB: Int
)
