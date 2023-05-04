package com.uniplat.uniplatapi.domain.dto.request.update

import com.uniplat.uniplatapi.validation.constraints.Latitude
import com.uniplat.uniplatapi.validation.constraints.Longitude
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import java.time.Instant

data class UpdatePostRequest(
    @get:Size(min = 1, max = 255, message = "error.post.description-invalid")
    val description: String?,

    var activityStartAt: Instant?,

    @get:Size(min = 1, max = 255, message = "error.post.activity-location-description-invalid")
    val activityLocationDescription: String?,

    @Latitude(message = "error.validation.constraints.Latitude.message")
    val latitude: BigDecimal?,

    @Longitude(message = "error.validation.constraints.Longitude.message")
    val longitude: BigDecimal?,

    val version: Int
)
