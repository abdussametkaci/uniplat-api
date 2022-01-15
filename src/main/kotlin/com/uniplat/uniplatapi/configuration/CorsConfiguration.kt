package com.uniplat.uniplatapi.configuration

import com.uniplat.uniplatapi.configuration.properties.CorsProperties
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
class CorsConfiguration(private val corsProperties: CorsProperties) : WebFluxConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins(*corsProperties.allowedOrigins.toTypedArray())
            .allowedMethods(
                HttpMethod.GET.name,
                HttpMethod.POST.name,
                HttpMethod.PUT.name,
                HttpMethod.PATCH.name,
                HttpMethod.HEAD.name,
                HttpMethod.OPTIONS.name,
                HttpMethod.DELETE.name
            )
            .allowedHeaders("*")
            .allowCredentials(true)
    }
}
