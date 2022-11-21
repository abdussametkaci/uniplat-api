package com.uniplat.uniplatapi.domain.model

import com.uniplat.uniplatapi.domain.enums.Gender
import com.uniplat.uniplatapi.domain.enums.UserType
import com.uniplat.uniplatapi.extensions.toAuthority
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.Instant
import java.util.UUID

@Table("\"user\"")
data class User(
    @Id val id: UUID? = null,
    var name: String,
    var surname: String,
    var gender: Gender,
    var birthDate: Instant,
    val email: String,
    private var password: String,
    var universityId: UUID? = null,
    val type: UserType,
    var description: String? = null,
    var profileImgId: UUID? = null,
    var messageAccessed: Boolean = true,
    var enabled: Boolean = false,
    @Version var version: Int? = null,
    @CreatedDate var createdAt: Instant? = null,
    @LastModifiedDate var lastModifiedAt: Instant? = null
) : UserDetails {

    override fun getAuthorities(): List<GrantedAuthority> {
        return type.toAuthority()
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    fun setPassword(password: String) {
        this.password = password
    }
}
