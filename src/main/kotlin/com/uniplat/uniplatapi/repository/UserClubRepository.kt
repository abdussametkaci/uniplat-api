package com.uniplat.uniplatapi.repository

import com.uniplat.uniplatapi.domain.model.UserClub
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface UserClubRepository : CoroutineCrudRepository<UserClub, UUID> {

    fun findAllBy(pageable: Pageable): Flow<UserClub>
}
