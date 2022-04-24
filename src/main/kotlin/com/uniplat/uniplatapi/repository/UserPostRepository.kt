package com.uniplat.uniplatapi.repository

import com.uniplat.uniplatapi.domain.model.UserPost
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface UserPostRepository : CoroutineCrudRepository<UserPost, UUID> {

    fun findAllBy(pageable: Pageable): Flow<UserPost>
}
