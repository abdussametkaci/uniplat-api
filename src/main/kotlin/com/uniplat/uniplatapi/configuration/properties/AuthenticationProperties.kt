package com.uniplat.uniplatapi.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.io.File

@ConstructorBinding
@ConfigurationProperties(prefix = "authentication")
data class AuthenticationProperties(
    val tokenIssuerId: String,
    val tokenExpirationTimeSeconds: Long
) {
    val authPublicKey: ByteArray = File("public.key").readBytes()
    val authPrivateKey: ByteArray = File("private.key").readBytes()
}
