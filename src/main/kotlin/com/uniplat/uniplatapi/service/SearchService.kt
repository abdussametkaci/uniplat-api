package com.uniplat.uniplatapi.service

import com.uniplat.uniplatapi.domain.dto.response.SearchResponse
import com.uniplat.uniplatapi.domain.enums.SearchType
import com.uniplat.uniplatapi.domain.enums.UserType
import com.uniplat.uniplatapi.domain.model.Club
import com.uniplat.uniplatapi.domain.model.Post
import com.uniplat.uniplatapi.domain.model.University
import com.uniplat.uniplatapi.domain.model.User
import com.uniplat.uniplatapi.exception.BadRequestException
import com.uniplat.uniplatapi.extensions.convert
import com.uniplat.uniplatapi.model.PaginatedResponse
import com.uniplat.uniplatapi.repository.ClubRepository
import com.uniplat.uniplatapi.repository.PostRepository
import com.uniplat.uniplatapi.repository.UniversityRepository
import com.uniplat.uniplatapi.repository.UserRepository
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext
import org.springframework.core.convert.ConversionService
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import kotlin.math.ceil

@Service
class SearchService(
    private val userRepository: UserRepository,
    private val universityRepository: UniversityRepository,
    private val clubRepository: ClubRepository,
    private val postRepository: PostRepository,
    private val conversionService: ConversionService
) {

    @OptIn(ObsoleteCoroutinesApi::class)
    suspend fun search(filters: List<SearchType>, text: String, pageable: Pageable): PaginatedResponse<SearchResponse> {
        validateUserFilter(filters)
        val size = if (filters.isNotEmpty()) pageable.pageSize / filters.size else pageable.pageSize / 4
        var count: Long = 0

        val responseList = mutableListOf<SearchResponse>()

        val filterConditions = SearchType.values().associateWith { filters.contains(it) }

        if (filters.isNotEmpty()) {
            coroutineScope {
                withContext(newSingleThreadContext("SearchContext")) {
                    if (filterConditions[SearchType.USER] == true) {
                        searchUsers(text, pageable, size)
                            .onEach { responseList.add(conversionService.convert(it)) }
                            .launchIn(this)
                    }
                    if (filterConditions[SearchType.TEACHER] == true) {
                        searchUsersByType(UserType.TEACHER, text, pageable, size)
                            .onEach { responseList.add(conversionService.convert(it)) }
                            .launchIn(this)
                    }
                    if (filterConditions[SearchType.STUDENT] == true) {
                        searchUsersByType(UserType.STUDENT, text, pageable, size)
                            .onEach { responseList.add(conversionService.convert(it)) }
                            .launchIn(this)
                    }
                    if (filterConditions[SearchType.UNIVERSITY] == true) {
                        searchUniversities(text, pageable, size)
                            .onEach { responseList.add(conversionService.convert(it)) }
                            .launchIn(this)
                    }
                    if (filterConditions[SearchType.CLUB] == true) {
                        searchClubs(text, pageable, size)
                            .onEach { responseList.add(conversionService.convert(it)) }
                            .launchIn(this)
                    }
                    if (filterConditions[SearchType.POST] == true) {
                        searchPosts(text, pageable, size)
                            .onEach { responseList.add(conversionService.convert(it)) }
                            .launchIn(this)
                    }

                    val countUser = async(start = CoroutineStart.LAZY) { countUsers(text) }
                    val countTeacher = async(start = CoroutineStart.LAZY) { countUsersByType(UserType.TEACHER, text) }
                    val countStudent = async(start = CoroutineStart.LAZY) { countUsersByType(UserType.STUDENT, text) }
                    val countUniversity = async(start = CoroutineStart.LAZY) { countUniversities(text) }
                    val countClub = async(start = CoroutineStart.LAZY) { countClubs(text) }
                    val countPost = async(start = CoroutineStart.LAZY) { countPosts(text) }

                    if (filterConditions[SearchType.USER] == true) countUser.start() else countUser.cancel()
                    if (filterConditions[SearchType.TEACHER] == true) countTeacher.start() else countTeacher.cancel()
                    if (filterConditions[SearchType.STUDENT] == true) countStudent.start() else countStudent.cancel()
                    if (filterConditions[SearchType.UNIVERSITY] == true) countUniversity.start() else countUniversity.cancel()
                    if (filterConditions[SearchType.CLUB] == true) countClub.start() else countClub.cancel()
                    if (filterConditions[SearchType.POST] == true) countPost.start() else countPost.cancel()

                    if (filterConditions[SearchType.USER] == true) count += countUser.await()
                    if (filterConditions[SearchType.TEACHER] == true) count += countTeacher.await()
                    if (filterConditions[SearchType.STUDENT] == true) count += countStudent.await()
                    if (filterConditions[SearchType.UNIVERSITY] == true) count += countUniversity.await()
                    if (filterConditions[SearchType.CLUB] == true) count += countClub.await()
                    if (filterConditions[SearchType.POST] == true) count += countPost.await()
                }
            }
        } else {
            coroutineScope {
                withContext(newSingleThreadContext("SearchContext")) {
                    searchUsers(text, pageable, size)
                        .onEach { responseList.add(conversionService.convert(it)) }
                        .launchIn(this)

                    searchUniversities(text, pageable, size)
                        .onEach { responseList.add(conversionService.convert(it)) }
                        .launchIn(this)

                    searchClubs(text, pageable, size)
                        .onEach { responseList.add(conversionService.convert(it)) }
                        .launchIn(this)

                    searchPosts(text, pageable, size)
                        .onEach { responseList.add(conversionService.convert(it)) }
                        .launchIn(this)

                    val countUser = async { countUsers(text) }
                    val countUniversity = async { countUniversities(text) }
                    val countClub = async { countClubs(text) }
                    val countPost = async { countPosts(text) }
                    awaitAll(countUser, countUniversity, countClub, countPost).forEach { count += it }
                }
            }
        }

        return PaginatedResponse(
            number = pageable.pageNumber,
            size = pageable.pageSize,
            totalElements = count,
            totalPages = ceil(count.toDouble() / pageable.pageSize).toInt(),
            content = responseList
        )
    }

    private fun searchUsers(text: String, pageable: Pageable, size: Int): Flow<User> {
        return userRepository.findAllByStartWith(text, pageable.offset, size)
    }

    private fun searchUsersByType(type: UserType, text: String, pageable: Pageable, size: Int): Flow<User> {
        return userRepository.findAllByTypeStartWith(type, text, pageable.offset, size)
    }

    private fun searchUniversities(text: String, pageable: Pageable, size: Int): Flow<University> {
        return universityRepository.findAllByStartWith(text, pageable.offset, size)
    }

    private fun searchClubs(text: String, pageable: Pageable, size: Int): Flow<Club> {
        return clubRepository.findAllByStartWith(text, pageable.offset, size)
    }

    private fun searchPosts(text: String, pageable: Pageable, size: Int): Flow<Post> {
        return postRepository.findAllByStartWith(text, pageable.offset, size)
    }

    private suspend fun countUsers(text: String): Long = userRepository.countByStartWith(text)

    private suspend fun countUsersByType(type: UserType, text: String): Long = userRepository.countByTypeStartWith(type, text)

    private suspend fun countUniversities(text: String): Long = universityRepository.countByStartWith(text)

    private suspend fun countClubs(text: String): Long = clubRepository.countByStartWith(text)

    private suspend fun countPosts(text: String): Long = postRepository.countByStartWith(text)

    private suspend fun validateUserFilter(filters: List<SearchType>) {
        if (filters.contains(SearchType.USER)) {
            if (filters.contains(SearchType.TEACHER) || filters.contains(SearchType.STUDENT)) throw BadRequestException("error.search.invalid")
        }
    }
}
