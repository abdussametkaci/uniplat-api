package com.uniplat.uniplatapi.configuration

import com.uniplat.uniplatapi.configuration.properties.DatabaseProperties
import com.uniplat.uniplatapi.domain.enums.Gender
import com.uniplat.uniplatapi.domain.enums.UserType
import com.uniplat.uniplatapi.extensions.enumConverterOf
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.postgresql.codec.EnumCodec
import io.r2dbc.postgresql.extension.CodecRegistrar
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.data.r2dbc.convert.EnumWriteSupport
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@Configuration
@EnableR2dbcRepositories
@EnableR2dbcAuditing
class DatabaseConfiguration(private val databaseProperties: DatabaseProperties) : AbstractR2dbcConfiguration() {

    @Bean
    override fun connectionFactory(): PostgresqlConnectionFactory {
        return PostgresqlConnectionFactory(
            with(databaseProperties) {
                PostgresqlConnectionConfiguration.builder()
                    .host(host)
                    .port(port.toInt())
                    .database(name)
                    .username(username)
                    .password(password)
                    .codecRegistrar(getCodecRegistrar())
                    .build()
            }
        )
    }

    fun getCodecRegistrar(): CodecRegistrar {
        return EnumCodec.builder()
            .withEnum("gender", Gender::class.java)
            .withEnum("user_type", UserType::class.java)
            .build()
    }

    override fun getCustomConverters(): List<EnumWriteSupport<out Enum<*>>> {
        return listOf(
            enumConverterOf<Gender>(),
            enumConverterOf<UserType>()
        )
    }
}
