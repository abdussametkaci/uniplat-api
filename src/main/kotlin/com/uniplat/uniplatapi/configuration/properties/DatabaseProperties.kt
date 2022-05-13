package com.uniplat.uniplatapi.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.time.Duration

@ConstructorBinding
@ConfigurationProperties(prefix = "database")
data class DatabaseProperties(
    val host: String,
    val port: String,
    val name: String,
    val username: String,
    val password: String,
    val connectionPool: ConnectionPoolProperties
)

data class ConnectionPoolProperties(
    val maxSize: Int,
    val initialSize: Int,
    val maxIdleTime: Duration,
    val maxCreateConnectionTime: Duration
)
