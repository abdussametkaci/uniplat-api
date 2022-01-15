package com.uniplat.uniplatapi.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "database")
data class DatabaseProperties(
    val host: String,
    val port: String,
    val name: String,
    val username: String,
    val password: String
)
