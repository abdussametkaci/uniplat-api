package com.uniplat.uniplatapi.repository

import com.uniplat.uniplatapi.domain.model.University
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface UniversityRepository : CoroutineCrudRepository<University, UUID> {

    suspend fun findByName(name: String): University?
}
