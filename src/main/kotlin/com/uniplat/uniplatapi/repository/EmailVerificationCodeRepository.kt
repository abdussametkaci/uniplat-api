package com.uniplat.uniplatapi.repository

import com.uniplat.uniplatapi.domain.model.EmailVerificationCode
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface EmailVerificationCodeRepository : CoroutineCrudRepository<EmailVerificationCode, UUID> {

    suspend fun findByCode(code: String): EmailVerificationCode?
}
