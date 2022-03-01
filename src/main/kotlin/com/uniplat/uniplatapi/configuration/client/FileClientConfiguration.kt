package com.uniplat.uniplatapi.configuration.client

import com.uniplat.uniplatapi.configuration.properties.FileClientProperties
import com.uniplat.uniplatapi.extensions.configureTimeouts
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class FileClientConfiguration {

    @Bean
    fun fileClient(fileClientProperties: FileClientProperties): WebClient {
        with(fileClientProperties) {
            return WebClient.builder()
                .baseUrl(baseUrl)
                .configureTimeouts(connectionTimeout, readTimeout)
                .build()
        }
    }
}
