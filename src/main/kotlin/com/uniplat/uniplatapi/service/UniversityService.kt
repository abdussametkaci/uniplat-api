package com.uniplat.uniplatapi.service

import com.uniplat.uniplatapi.domain.model.University
import com.uniplat.uniplatapi.exception.NotFoundException
import com.uniplat.uniplatapi.repository.UniversityRepository
import org.springframework.stereotype.Service

@Service
class UniversityService(private val universityRepository: UniversityRepository) {

    suspend fun getByName(name: String): University {
        return universityRepository.findByName(name) ?: throw NotFoundException("error.university.not-found", args = arrayOf(name))
    }
}
