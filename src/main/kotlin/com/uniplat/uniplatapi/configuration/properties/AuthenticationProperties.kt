package com.uniplat.uniplatapi.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import java.io.File

@ConfigurationProperties(prefix = "authentication")
data class AuthenticationProperties(
    val tokenIssuerId: String,
    val tokenExpirationTimeSeconds: Long
) {
    val authPublicKey: ByteArray = File("public.key").readBytes()
    val authPrivateKey: ByteArray = File("private.key").readBytes()
}
