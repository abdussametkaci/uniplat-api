package com.uniplat.uniplatapi.domain.dto.request.update

import java.math.BigDecimal
import java.time.Instant
import javax.validation.constraints.Size

data class UpdatePostRequest(
    @get:Size(min = 1, max = 255, message = "error.post.description-invalid")
    val description: String?,

    var activityStartAt: Instant?,

    @get:Size(min = 1, max = 255, message = "error.post.activity-location-description-invalid")
    val activityLocationDescription: String?,

    val latitude: BigDecimal?,
    val longitude: BigDecimal?,
    val version: Int
)
