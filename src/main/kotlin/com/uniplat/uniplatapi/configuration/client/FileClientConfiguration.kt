package com.uniplat.uniplatapi.configuration.client

import com.uniplat.uniplatapi.configuration.properties.FileClientProperties
import com.uniplat.uniplatapi.extensions.configureTimeouts
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.ClientCodecConfigurer
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class FileClientConfiguration {

    @Bean
    fun fileClient(fileClientProperties: FileClientProperties): WebClient {
        with(fileClientProperties) {
            val size = memorySizeByMB * 1024 * 1024
            val strategies = ExchangeStrategies.builder()
                .codecs { codecs: ClientCodecConfigurer ->
                    codecs.defaultCodecs().maxInMemorySize(size)
                }
                .build()
            return WebClient.builder()
                .exchangeStrategies(strategies)
                .baseUrl(baseUrl)
                .configureTimeouts(connectionTimeout, readTimeout)
                .build()
        }
    }
}
