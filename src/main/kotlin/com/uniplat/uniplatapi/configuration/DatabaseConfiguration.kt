package com.uniplat.uniplatapi.configuration

import com.uniplat.uniplatapi.configuration.properties.DatabaseProperties
import com.uniplat.uniplatapi.domain.enums.Gender
import com.uniplat.uniplatapi.domain.enums.OwnerType
import com.uniplat.uniplatapi.domain.enums.PostType
import com.uniplat.uniplatapi.domain.enums.UserType
import com.uniplat.uniplatapi.extensions.enumConverterOf
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.postgresql.codec.EnumCodec
import io.r2dbc.postgresql.extension.CodecRegistrar
import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.data.r2dbc.convert.EnumWriteSupport
import org.springframework.data.r2dbc.core.DefaultReactiveDataAccessStrategy
import org.springframework.data.r2dbc.core.R2dbcEntityOperations
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.r2dbc.dialect.PostgresDialect
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.r2dbc.core.DatabaseClient

@Configuration
@EnableR2dbcRepositories(
    entityOperationsRef = "databaseTemplate",
    basePackages = ["com.uniplat.uniplatapi.repository"]
)
@EnableR2dbcAuditing
class DatabaseConfiguration(private val databaseProperties: DatabaseProperties) {

    @Bean
    fun connectionFactory(): PostgresqlConnectionFactory {
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

    @Bean
    fun databaseClient(connectionFactory: ConnectionFactory): DatabaseClient {
        return DatabaseClient.builder()
            .connectionFactory(connectionFactory)
            .bindMarkers(PostgresDialect.INSTANCE.bindMarkersFactory)
            .build()
    }

    @Bean
    fun databaseTemplate(databaseClient: DatabaseClient): R2dbcEntityOperations {
        val strategy = DefaultReactiveDataAccessStrategy(PostgresDialect.INSTANCE, getCustomConverters())
        return R2dbcEntityTemplate(databaseClient, strategy)
    }

    fun getCodecRegistrar(): CodecRegistrar {
        return EnumCodec.builder()
            .withEnum("gender", Gender::class.java)
            .withEnum("user_type", UserType::class.java)
            .withEnum("owner_type", OwnerType::class.java)
            .withEnum("post_type", PostType::class.java)
            .build()
    }

    fun getCustomConverters(): List<EnumWriteSupport<out Enum<*>>> {
        return listOf(
            enumConverterOf<Gender>(),
            enumConverterOf<UserType>(),
            enumConverterOf<OwnerType>(),
            enumConverterOf<PostType>()
        )
    }
}
