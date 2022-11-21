package com.uniplat.uniplatapi.repository

import com.uniplat.uniplatapi.domain.model.Session
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface SessionRepository : CoroutineCrudRepository<Session, UUID> {

    suspend fun findByUserId(userId: UUID): Session?

    suspend fun deleteByUserId(userId: UUID)
}
