package com.uniplat.uniplatapi.configuration.security

import com.uniplat.uniplatapi.configuration.security.auth.JwtAuthenticationConverter
import com.uniplat.uniplatapi.configuration.security.auth.JwtReactiveAuthenticationManager
import com.uniplat.uniplatapi.configuration.security.auth.JwtServerAuthenticationFailureHandler
import com.uniplat.uniplatapi.service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.WebFilterChainServerAuthenticationSuccessHandler
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
@EnableWebFlux
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity(useAuthorizationManager = false)
class WebSecurityConfig : WebFluxConfigurer {

    @Bean
    fun configureSecurity(
        http: ServerHttpSecurity,
        jwtAuthenticationFilter: AuthenticationWebFilter
    ): SecurityWebFilterChain {
        return http
            .csrf().disable()
            .authorizeExchange()
            .pathMatchers(*EXCLUDED_PATHS).permitAll()
            .pathMatchers(HttpMethod.GET, "/universities").permitAll()
            .anyExchange().authenticated()
            .and()
            .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationWebFilter(
        jwtReactiveAuthenticationManager: JwtReactiveAuthenticationManager,
        jwtAuthenticationConverter: JwtAuthenticationConverter,
        jwtServerAuthenticationFailureHandler: JwtServerAuthenticationFailureHandler
    ): AuthenticationWebFilter {
        return AuthenticationWebFilter(jwtReactiveAuthenticationManager).apply {
            setServerAuthenticationConverter(jwtAuthenticationConverter)
            setAuthenticationSuccessHandler(WebFilterChainServerAuthenticationSuccessHandler())
            setAuthenticationFailureHandler(jwtServerAuthenticationFailureHandler)
        }
    }

    @Bean
    @Primary
    fun userDetailsRepositoryReactiveAuthenticationManager(
        userService: UserService,
        passwordEncoder: PasswordEncoder
    ): ReactiveAuthenticationManager {
        return UserDetailsRepositoryReactiveAuthenticationManager(userService).apply {
            setPasswordEncoder(passwordEncoder)
        }
    }

    private companion object {
        val EXCLUDED_PATHS = arrayOf(
            "/",
            "/actuator/**",
            "/webjars/**",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/auth/login",
            "/users/register",
            "/email/**"
        )
    }
}
