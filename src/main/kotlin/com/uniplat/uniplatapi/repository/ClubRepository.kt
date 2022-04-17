package com.uniplat.uniplatapi.repository

import com.uniplat.uniplatapi.domain.model.Club
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface ClubRepository : CoroutineCrudRepository<Club, UUID> {

    fun findAll(pageable: Pageable): Flow<Club>
}
