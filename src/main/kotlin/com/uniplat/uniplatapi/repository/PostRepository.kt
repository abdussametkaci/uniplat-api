package com.uniplat.uniplatapi.repository

import com.uniplat.uniplatapi.domain.model.Post
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface PostRepository : CoroutineCrudRepository<Post, UUID> {

    fun findAll(pageable: Pageable): Flow<Post>
}
