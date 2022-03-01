package com.uniplat.uniplatapi.client.file.dto.response

import java.time.Instant
import java.util.UUID

data class FileResponse(
    val id: UUID,
    val extension: String,
    val createdAt: Instant
)
