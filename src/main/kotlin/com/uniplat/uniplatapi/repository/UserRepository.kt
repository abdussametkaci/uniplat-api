package com.uniplat.uniplatapi.repository

import com.uniplat.uniplatapi.domain.model.User
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface UserRepository : CoroutineCrudRepository<User, UUID> {

    suspend fun findByEmailAndPassword(email: String, password: String): User?

    fun findAllBy(pageable: Pageable): Flow<User>
}
