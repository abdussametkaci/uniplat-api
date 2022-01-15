package com.uniplat.uniplatapi.configuration

import com.uniplat.uniplatapi.component.ContextAwareConversionService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter

@Configuration
class ConversionConfiguration {

    @Bean
    fun conversionService(converters: Set<Converter<*, *>>): ContextAwareConversionService {
        return ContextAwareConversionService().registerConverters(converters)
    }
}
