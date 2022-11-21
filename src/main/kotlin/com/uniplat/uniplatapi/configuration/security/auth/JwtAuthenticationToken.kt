package com.uniplat.uniplatapi.configuration.security.auth

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class JwtAuthenticationToken(
    private val jwtToken: String,
    private val principal: Any? = null,
    private val authorities: List<GrantedAuthority> = emptyList(),
    private var authenticated: Boolean = false
) : Authentication {

    override fun getName(): String? {
        return null
    }

    override fun getAuthorities(): List<GrantedAuthority> {
        return authorities
    }

    override fun getCredentials(): Any {
        return jwtToken
    }

    override fun getDetails(): Any? {
        return principal
    }

    override fun getPrincipal(): Any? {
        return principal
    }

    override fun isAuthenticated(): Boolean {
        return authenticated
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
        this.authenticated = isAuthenticated
    }
}
