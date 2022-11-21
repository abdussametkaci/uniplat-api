package com.uniplat.uniplatapi.service

import com.uniplat.uniplatapi.domain.model.Session
import com.uniplat.uniplatapi.repository.SessionRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class SessionService(private val sessionRepository: SessionRepository) {

    suspend fun findByUserId(userId: UUID) = sessionRepository.findByUserId(userId)

    suspend fun save(session: Session) = sessionRepository.save(session)

    suspend fun deleteByUserId(userId: UUID) = sessionRepository.deleteByUserId(userId)
}
