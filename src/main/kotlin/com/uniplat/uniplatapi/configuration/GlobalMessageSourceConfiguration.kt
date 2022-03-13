package com.uniplat.uniplatapi.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import java.util.Locale

@Configuration
class GlobalMessageSourceConfiguration {

    @Bean
    fun messageSource(): ReloadableResourceBundleMessageSource =
        ReloadableResourceBundleMessageSource().apply {
            setBasename("classpath:messages")
            setDefaultEncoding("UTF-8")
            setDefaultLocale(Locale("en"))
            setUseCodeAsDefaultMessage(true)
        }
}
