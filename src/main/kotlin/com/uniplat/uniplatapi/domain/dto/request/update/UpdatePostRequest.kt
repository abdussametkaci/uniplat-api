package com.uniplat.uniplatapi.domain.dto.request.update

import java.time.Instant
import javax.validation.constraints.Size

data class UpdatePostRequest(
    @get:Size(min = 1, max = 255, message = "error.post.description-invalid")
    val description: String?,

    var activityStartAt: Instant?,
    val version: Int
)
