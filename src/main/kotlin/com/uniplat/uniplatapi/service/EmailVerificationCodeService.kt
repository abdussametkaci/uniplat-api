package com.uniplat.uniplatapi.service

import com.uniplat.uniplatapi.configuration.properties.EmailProperties
import com.uniplat.uniplatapi.domain.model.EmailVerificationCode
import com.uniplat.uniplatapi.domain.model.User
import com.uniplat.uniplatapi.exception.BadRequestException
import com.uniplat.uniplatapi.exception.NotFoundException
import com.uniplat.uniplatapi.repository.EmailVerificationCodeRepository
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.util.UUID
import javax.mail.internet.MimeMessage

@Service
class EmailVerificationCodeService(
    private val emailVerificationCodeRepository: EmailVerificationCodeRepository,
    private val userService: UserService,
    private val javaMailSender: JavaMailSender,
    private val emailProperties: EmailProperties
) {

    suspend fun verifyUser(code: String): String {
        emailVerificationCodeRepository.findByCode(code)
            ?.let { emailVerificationCode ->
                userService.getById(emailVerificationCode.userId)
                    .also { validate(it.id!!, emailVerificationCode.userId) }
                    .apply { enabled = true }
                    .let { userService.save(it) }
            }
            ?: throw NotFoundException("error.email-verification-code.not-found", args = listOf(code))

        return "Verified Account"
    }

    suspend fun saveAndSendVerificationEmail(user: User, url: String) {
        sendVerificationEmail(user, url, save(user.id!!).code)
    }

    private suspend fun sendVerificationEmail(user: User, url: String, code: String) {
        val toAddress: String = user.email
        val fromAddress = emailProperties.username
        val senderName = "UniPlat"
        val subject = "Please verify your registration"
        var content = """
            Dear [[name]],<br>
            Please click the link below to verify your registration:<br>
            <h3><a href="[[URL]]" target="_self">VERIFY</a></h3>
            Thank you,<br>
            UniPlat
        """.trimIndent()

        val message: MimeMessage = javaMailSender.createMimeMessage()
        val helper = MimeMessageHelper(message)

        helper.setFrom(fromAddress, senderName)
        helper.setTo(toAddress)
        helper.setSubject(subject)

        content = content.replace("[[name]]", "${user.name} ${user.surname}")
        val verifyURL = "$url/verify?code=$code"

        content = content.replace("[[URL]]", verifyURL)

        helper.setText(content, true)

        javaMailSender.send(message)
    }

    private suspend fun save(userId: UUID): EmailVerificationCode {
        return emailVerificationCodeRepository.save(EmailVerificationCode(userId = userId, code = RandomStringUtils.randomAlphabetic(64)))
    }

    private suspend fun validate(userId: UUID, userIdOfVerificationCode: UUID) {
        if (userId != userIdOfVerificationCode) throw BadRequestException("error.email-verification-code.invalid")
    }
}
