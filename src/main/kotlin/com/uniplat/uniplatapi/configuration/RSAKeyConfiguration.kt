package com.uniplat.uniplatapi.configuration

import org.springframework.context.annotation.Configuration
import java.io.File
import java.security.KeyPairGenerator
import javax.annotation.PostConstruct

@Configuration
class RSAKeyConfiguration {

    @PostConstruct
    fun generateRSAKeyPair() {
        val generator = KeyPairGenerator.getInstance("RSA")
        generator.initialize(2048)
        val pair = generator.generateKeyPair()

        File("public.key")
            .takeIf { !it.exists() }
            ?.writeBytes(pair.public.encoded)

        File("private.key")
            .takeIf { !it.exists() }
            ?.writeBytes(pair.private.encoded)
    }
}
