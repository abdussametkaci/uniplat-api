package com.uniplat.uniplatapi.configuration

import com.uniplat.uniplatapi.configuration.properties.DatabaseProperties
import org.flywaydb.core.Flyway
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FlywayConfiguration(private val databaseProperties: DatabaseProperties) {

    @Bean(initMethod = "migrate")
    fun flyway(): Flyway {
        return Flyway(
            with(databaseProperties) {
                Flyway.configure()
                    .baselineOnMigrate(true)
                    .outOfOrder(true)
                    .dataSource(
                        //getDatabaseUrl(host, port, name),
                        getHerokuDatabaseURL(),
                        username,
                        password
                    )
            }
        )
    }

    fun getDatabaseUrl(host: String, port: String, name: String): String {
        return "jdbc:postgresql://$host:$port/$name"
    }

    fun getHerokuDatabaseURL(): String {
        return "postgres://naeneeqjlpzqvc:bf5aceaccdbd12f84c92138f3de9a293935c47693c69b393569a1fb8e3febc91@ec2-52-208-221-89.eu-west-1.compute.amazonaws.com:5432/d6c4h4ktogn7nf"
    }
}
