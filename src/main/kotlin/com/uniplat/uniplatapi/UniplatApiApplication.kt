package com.uniplat.uniplatapi

import com.uniplat.uniplatapi.configuration.GlobalMessageSourceConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@ConfigurationPropertiesScan
@Import(GlobalMessageSourceConfiguration::class)
class UniplatApiApplication

fun main(args: Array<String>) {
    runApplication<UniplatApiApplication>(*args)
}
