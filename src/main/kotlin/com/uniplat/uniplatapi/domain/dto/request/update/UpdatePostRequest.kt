package com.uniplat.uniplatapi.domain.dto.request.update

import javax.validation.constraints.Size

data class UpdatePostRequest(
    @get:Size(min = 1, max = 255, message = "error.post.description-invalid")
    val description: String?,

    val version: Int
)
