package com.uniplat.uniplatapi.service

import com.uniplat.uniplatapi.domain.dto.request.create.CreateUserRequest
import com.uniplat.uniplatapi.domain.dto.request.update.UpdateUserPasswordRequest
import com.uniplat.uniplatapi.domain.dto.request.update.UpdateUserRequest
import com.uniplat.uniplatapi.domain.enums.OwnerType
import com.uniplat.uniplatapi.domain.enums.UserType
import com.uniplat.uniplatapi.domain.model.User
import com.uniplat.uniplatapi.domain.model.UserDTO
import com.uniplat.uniplatapi.exception.ConflictException
import com.uniplat.uniplatapi.exception.NotFoundException
import com.uniplat.uniplatapi.extensions.saveUnique
import com.uniplat.uniplatapi.model.PaginatedModel
import com.uniplat.uniplatapi.repository.UserDTORepository
import com.uniplat.uniplatapi.repository.UserRepository
import com.uniplat.uniplatapi.validation.UserValidator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactor.mono
import org.springframework.data.domain.Pageable
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userDTORepository: UserDTORepository,
    private val postService: PostService,
    private val userLikedPostService: UserLikedPostService,
    private val userFollowService: UserFollowService,
    private val postCommentService: PostCommentService,
    private val fileService: FileService,
    private val activityParticipantService: ActivityParticipantService,
    private val emailVerificationCodeService: EmailVerificationCodeService,
    private val userValidator: UserValidator,
    private val passwordEncoder: PasswordEncoder,
    private val applicationScope: CoroutineScope
) : ReactiveUserDetailsService {

    suspend fun getAll(pageable: Pageable): PaginatedModel<User> {
        val count = userRepository.count()
        val users = userRepository.findAllBy(pageable)

        return PaginatedModel(
            content = users,
            number = pageable.pageNumber,
            size = pageable.pageSize,
            totalElements = count
        )
    }

    suspend fun getAll(userId: UUID, pageable: Pageable): PaginatedModel<UserDTO> {
        val count = userRepository.countByEnabledIsTrue()
        val users = userDTORepository.findAllBy(userId, pageable.offset, pageable.pageSize)

        return PaginatedModel(
            content = users,
            number = pageable.pageNumber,
            size = pageable.pageSize,
            totalElements = count
        )
    }

    suspend fun getById(id: UUID, userId: UUID): UserDTO {
        return userDTORepository.findById(id, userId) ?: throw NotFoundException("error.user.not-found", args = listOf(id))
    }

    suspend fun getById(id: UUID): User {
        return userRepository.findById(id) ?: throw NotFoundException("error.user.not-found", args = listOf(id))
    }

    suspend fun save(user: User): User {
        return userRepository.save(user)
    }

    suspend fun create(request: CreateUserRequest, url: String): User {
        with(request) {
            val user = User(
                name = name,
                surname = surname,
                gender = gender,
                birthDate = birthDate,
                email = email,
                password = passwordEncoder.encode(password),
                universityId = universityId,
                type = if ("@stu" in email) UserType.STUDENT else UserType.TEACHER,
                description = description,
                profileImgId = profileImgId,
                messageAccessed = messageAccessed
            )

            val savedUser = userRepository.saveUnique(user) { throw ConflictException("error.user.conflict", args = listOf(email)) }

            emailVerificationCodeService.saveAndSendVerificationEmail(savedUser.id!!, url)

            return savedUser
        }
    }

    suspend fun update(id: UUID, request: UpdateUserRequest): UserDTO {
        return getById(id)
            .apply {
                with(request) {
                    this@apply.name = name
                    this@apply.surname = surname
                    this@apply.gender = gender
                    this@apply.birthDate = birthDate
                    this@apply.universityId = universityId
                    this@apply.description = description
                    this@apply.profileImgId = profileImgId
                    this@apply.messageAccessed = messageAccessed
                    this@apply.version = version
                }
            }
            .let { userRepository.save(it) }
            .let { getById(id, id) }
    }

    /*
    suspend fun update(id: UUID, request: UpdateUserRequest): User {
        return getById(id)
            .apply {
                with(request) {
                    this@apply.name = name
                    this@apply.surname = surname
                    this@apply.gender = gender
                    this@apply.birthDate = birthDate
                    this@apply.universityId = universityId
                    this@apply.description = description
                    this@apply.profileImgId = profileImgId
                    this@apply.messageAccessed = messageAccessed
                    this@apply.version = version
                }
            }
            .let { userRepository.save(it) }
    }
     */

    suspend fun updatePassword(id: UUID, request: UpdateUserPasswordRequest): User {
        return getById(id)
            .also { userValidator.validate(it, request) }
            .apply {
                with(request) {
                    this@apply.password = passwordEncoder.encode(newPassword)
                    this@apply.version = version
                }
            }
            .let { userRepository.save(it) }
    }

    @Transactional
    suspend fun delete(id: UUID) {
        userRepository.deleteAndReturnById(id)?.let { user ->
            postService.deleteAndReturnAllByOwnerIdAndOwnerType(id, OwnerType.USER)
                .onEach { post -> post.imgId?.let { imgId -> fileService.delete(imgId) } }
                .launchIn(applicationScope)

            applicationScope.launch {
                launch { user.profileImgId?.let { profileImgId -> fileService.delete(profileImgId) } }
                launch { userLikedPostService.deleteAllByUserId(id) }
                launch { userFollowService.deleteAllByUserId(id) }
                launch { userFollowService.deleteAllByFollowIdAndFollowType(id, OwnerType.USER) }
                launch { postCommentService.deleteAllByUserId(id) }
                launch { activityParticipantService.deleteAllByUserId(id) }
            }
        }
    }

    override fun findByUsername(username: String): Mono<UserDetails> = mono {
        userRepository.findByEmail(username)?.also {
            if (!it.enabled) throw NotFoundException("error.user.not-found-by-email", args = listOf(username))
        } ?: throw NotFoundException("error.user.not-found-by-email", args = listOf(username))
    }
}
