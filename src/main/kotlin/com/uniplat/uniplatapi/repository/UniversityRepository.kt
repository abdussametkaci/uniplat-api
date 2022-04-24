package com.uniplat.uniplatapi.repository

import com.uniplat.uniplatapi.domain.model.University
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface UniversityRepository : CoroutineCrudRepository<University, UUID> {

    fun findAllBy(pageable: Pageable): Flow<University>
}
