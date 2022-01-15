package com.uniplat.uniplatapi.repository

import com.uniplat.uniplatapi.domain.model.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface UserRepository : CoroutineCrudRepository<User, UUID> {

    suspend fun findByEmailAndPassword(email: String, password: String): User?

    suspend fun existsByEmail(email: String): Boolean
}
