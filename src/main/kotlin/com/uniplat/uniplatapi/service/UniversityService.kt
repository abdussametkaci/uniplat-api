package com.uniplat.uniplatapi.service

import com.uniplat.uniplatapi.domain.dto.request.create.CreateUniversityRequest
import com.uniplat.uniplatapi.domain.dto.request.update.UpdateUniversityRequest
import com.uniplat.uniplatapi.domain.enums.OwnerType
import com.uniplat.uniplatapi.domain.model.University
import com.uniplat.uniplatapi.domain.model.UniversityDTO
import com.uniplat.uniplatapi.exception.ConflictException
import com.uniplat.uniplatapi.exception.NotFoundException
import com.uniplat.uniplatapi.extensions.saveUnique
import com.uniplat.uniplatapi.model.PaginatedModel
import com.uniplat.uniplatapi.repository.UniversityDTORepository
import com.uniplat.uniplatapi.repository.UniversityRepository
import com.uniplat.uniplatapi.validation.UniversityValidator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UniversityService(
    private val universityRepository: UniversityRepository,
    private val universityDTORepository: UniversityDTORepository,
    private val postService: PostService,
    private val fileService: FileService,
    private val userFollowService: UserFollowService,
    private val universityValidator: UniversityValidator,
    private val applicationScope: CoroutineScope
) {

    suspend fun getAll(pageable: Pageable): PaginatedModel<University> {
        val count = universityRepository.count()
        val universities = universityRepository.findAllBy(pageable)

        return PaginatedModel(
            content = universities,
            number = pageable.pageNumber,
            size = pageable.pageSize,
            totalElements = count
        )
    }

    suspend fun getAll(userId: UUID?, adminId: UUID?, pageable: Pageable): PaginatedModel<UniversityDTO> {
        val count = universityRepository.count(adminId)
        val universities = universityDTORepository.findAllBy(userId, adminId, pageable.offset, pageable.pageSize)

        return PaginatedModel(
            content = universities,
            number = pageable.pageNumber,
            size = pageable.pageSize,
            totalElements = count
        )
    }

    suspend fun getById(id: UUID, userId: UUID): UniversityDTO {
        return universityDTORepository.findById(id, userId) ?: throw NotFoundException("error.university.not-found", args = listOf(id))
    }

    suspend fun getById(id: UUID): University {
        return universityRepository.findById(id) ?: throw NotFoundException("error.university.not-found", args = listOf(id))
    }

    suspend fun create(request: CreateUniversityRequest, userId: UUID): University {
        with(request) {
            val university = University(
                name = name,
                description = description,
                profileImgId = profileImgId,
                adminId = userId
            )

            return universityRepository.saveUnique(university) { throw ConflictException("error.university.conflict", args = listOf(name)) }
        }
    }

    suspend fun update(id: UUID, request: UpdateUniversityRequest, userId: UUID): UniversityDTO {
        universityValidator.validate(request.adminId)

        return getById(id)
            .apply {
                with(request) {
                    this@apply.name = name
                    this@apply.description = description
                    this@apply.profileImgId = profileImgId
                    this@apply.adminId = adminId
                    this@apply.version = version
                }
            }
            .let { universityRepository.saveUnique(it) { throw ConflictException("error.university.conflict", args = listOf(it.name)) } }
            .let { getById(id, userId) }
    }

    suspend fun update(id: UUID, request: UpdateUniversityRequest): University {
        universityValidator.validate(request.adminId)

        return getById(id)
            .apply {
                with(request) {
                    this@apply.name = name
                    this@apply.description = description
                    this@apply.profileImgId = profileImgId
                    this@apply.adminId = adminId
                    this@apply.version = version
                }
            }
            .let { universityRepository.saveUnique(it) { throw ConflictException("error.university.conflict", args = listOf(it.name)) } }
    }

    @Transactional
    suspend fun delete(id: UUID) {
        universityRepository.deleteAndReturnById(id)?.let { university ->
            postService.deleteAndReturnAllByOwnerIdAndOwnerType(id, OwnerType.UNIVERSITY)
                .onEach { post -> post.imgId?.let { imgId -> fileService.delete(imgId) } }
                .launchIn(applicationScope)

            applicationScope.launch {
                launch { university.profileImgId?.let { profileImgId -> fileService.delete(profileImgId) } }
                launch { userFollowService.deleteAllByFollowIdAndFollowType(id, OwnerType.USER) }
            }
        }
    }
}
