package com.uniplat.uniplatapi.service

import com.uniplat.uniplatapi.domain.dto.request.create.CreateClubRequest
import com.uniplat.uniplatapi.domain.dto.request.update.UpdateClubRequest
import com.uniplat.uniplatapi.domain.enums.OwnerType
import com.uniplat.uniplatapi.domain.model.Club
import com.uniplat.uniplatapi.domain.model.ClubDTO
import com.uniplat.uniplatapi.exception.ConflictException
import com.uniplat.uniplatapi.exception.NotFoundException
import com.uniplat.uniplatapi.extensions.saveUnique
import com.uniplat.uniplatapi.model.PaginatedModel
import com.uniplat.uniplatapi.repository.ClubDTORepository
import com.uniplat.uniplatapi.repository.ClubRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class ClubService(
    private val clubRepository: ClubRepository,
    private val clubDTORepository: ClubDTORepository,
    private val postService: PostService,
    private val fileService: FileService,
    private val userFollowService: UserFollowService,
    private val applicationScope: CoroutineScope
) {

    suspend fun getAll(universityId: UUID?, adminId: UUID?, pageable: Pageable): PaginatedModel<Club> {
        val count = clubRepository.count(universityId, adminId)
        val clubs = clubRepository.findAllBy(universityId, adminId, pageable.offset, pageable.pageSize)

        return PaginatedModel(
            content = clubs,
            number = pageable.pageNumber,
            size = pageable.pageSize,
            totalElements = count
        )
    }

    suspend fun getAll(userId: UUID, universityId: UUID?, adminId: UUID?, pageable: Pageable): PaginatedModel<ClubDTO> {
        val count = clubRepository.count(universityId, adminId)
        val clubs = clubDTORepository.findAllBy(userId, universityId, adminId, pageable.offset, pageable.pageSize)

        return PaginatedModel(
            content = clubs,
            number = pageable.pageNumber,
            size = pageable.pageSize,
            totalElements = count
        )
    }

    suspend fun getById(id: UUID, userId: UUID): ClubDTO {
        return clubDTORepository.findById(id, userId) ?: throw NotFoundException("error.club.not-found", args = listOf(id))
    }

    suspend fun getById(id: UUID): Club {
        return clubRepository.findById(id) ?: throw NotFoundException("error.club.not-found", args = listOf(id))
    }

    suspend fun create(request: CreateClubRequest, userId: UUID): Club {
        with(request) {
            val club = Club(
                name = name,
                universityId = universityId,
                description = description,
                profileImgId = profileImgId,
                adminId = userId
            )

            return clubRepository.saveUnique(club) { throw ConflictException("error.club.conflict", args = listOf(name, universityId)) }
        }
    }

    suspend fun update(id: UUID, request: UpdateClubRequest): Club {
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
            .let { clubRepository.saveUnique(it) { throw ConflictException("error.club.conflict", args = listOf(it.name, it.universityId)) } }
    }

    suspend fun update(id: UUID, request: UpdateClubRequest, userId: UUID): ClubDTO {
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
            .let { clubRepository.saveUnique(it) { throw ConflictException("error.club.conflict", args = listOf(it.name, it.universityId)) } }
            .let { getById(id, userId) }
    }

    @Transactional
    suspend fun delete(id: UUID) {
        clubRepository.deleteAndReturnById(id)?.let { club ->
            postService.deleteAndReturnAllByOwnerIdAndOwnerType(id, OwnerType.CLUB)
                .onEach { post -> post.imgId?.let { imgId -> fileService.delete(imgId) } }
                .launchIn(applicationScope)

            applicationScope.launch {
                launch { club.profileImgId?.let { profileImgId -> fileService.delete(profileImgId) } }
                launch { userFollowService.deleteAllByFollowIdAndFollowType(id, OwnerType.USER) }
            }
        }
    }
}
