package com.uniplat.uniplatapi.repository

import com.uniplat.uniplatapi.domain.model.ClubUser
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface ClubUserRepository : CoroutineCrudRepository<ClubUser, UUID> {

    fun findAll(pageable: Pageable): Flow<ClubUser>
}
