package com.uniplat.uniplatapi.service

import com.uniplat.uniplatapi.configuration.properties.AuthenticationProperties
import com.uniplat.uniplatapi.domain.enums.UserType
import com.uniplat.uniplatapi.exception.UnauthorizedException
import com.uniplat.uniplatapi.extensions.getUserId
import com.uniplat.uniplatapi.extensions.toDate
import com.uniplat.uniplatapi.extensions.toUUID
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Service
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.time.Instant
import java.util.UUID

@Service
class JwtService(
    private val sessionService: SessionService,
    private val authenticationProperties: AuthenticationProperties
) {

    fun getClaims(token: String): Claims {
        val publicKeySpec = X509EncodedKeySpec(authenticationProperties.authPublicKey)
        val keyFactory = KeyFactory.getInstance("RSA")
        val publicKey = keyFactory.generatePublic(publicKeySpec)

        return Jwts.parser()
            .setSigningKey(publicKey)
            .requireIssuer(authenticationProperties.tokenIssuerId)
            .parseClaimsJws(token)
            .body ?: throw IllegalStateException(INVALID_TOKEN)
    }

    fun generateToken(userId: String, email: String, userRoles: List<UserType>): String {
        val issuedAt = Instant.now()
        val expiresAt = issuedAt.plusSeconds(authenticationProperties.tokenExpirationTimeSeconds)
        val claims = mapOf(
            "userId" to userId,
            "roles" to userRoles
        )

        val privateKeySpec = PKCS8EncodedKeySpec(authenticationProperties.authPrivateKey)
        val keyFactory = KeyFactory.getInstance("RSA")
        val privateKey = keyFactory.generatePrivate(privateKeySpec)

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(issuedAt.toDate())
            .setExpiration(expiresAt.toDate())
            .setIssuer(authenticationProperties.tokenIssuerId)
            .signWith(SignatureAlgorithm.RS256, privateKey)
            .setSubject(email)
            .setId(UUID.randomUUID().toString())
            .compact()
    }

    suspend fun verify(token: String): Boolean {
        val claims = getClaims(token)
        val jti = claims.id
        val userId = claims.getUserId()

        sessionService.findByUserId(userId.toUUID())?.let {
            return jti.toUUID() == it.jti
        } ?: throw UnauthorizedException(AUTH_REQUIRED)
    }

    private companion object {
        const val INVALID_TOKEN = "error.token.invalid"
        const val AUTH_REQUIRED = "error.auth.required"
    }
}
