package com.uniplat.uniplatapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class UniplatApiApplication

fun main(args: Array<String>) {
    runApplication<UniplatApiApplication>(*args)
}
