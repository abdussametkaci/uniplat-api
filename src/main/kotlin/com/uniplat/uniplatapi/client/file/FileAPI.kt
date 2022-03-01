package com.uniplat.uniplatapi.client.file

import com.uniplat.uniplatapi.client.file.dto.response.FileResponse
import com.uniplat.uniplatapi.extensions.exceptionHandler
import org.springframework.core.io.Resource
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpEntity
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Component
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodilessEntity
import org.springframework.web.reactive.function.client.awaitBody
import java.util.UUID

@Component
class FileAPI(private val fileClient: WebClient) {

    suspend fun upload(filePart: FilePart): FileResponse {
        return fileClient.post()
            .uri(FILES_PATH)
            .body(BodyInserters.fromMultipartData(fromFile(filePart)))
            .retrieve()
            .exceptionHandler()
            .awaitBody()
    }

    suspend fun download(id: UUID): Resource {
        return fileClient.get()
            .uri("$FILES_PATH/{id}", id)
            .retrieve()
            .exceptionHandler()
            .awaitBody()
    }

    suspend fun delete(id: UUID) {
        fileClient.delete()
            .uri { it.path("$FILES_PATH/{id}").build(id) }
            .retrieve()
            .exceptionHandler()
            .awaitBodilessEntity()
    }

    private companion object {
        const val FILES_PATH = "/files"
    }

    private fun fromFile(filePart: FilePart): MultiValueMap<String?, HttpEntity<*>?> {
        val builder = MultipartBodyBuilder()
        builder.asyncPart("file", filePart.content(), DataBuffer::class.java).filename(filePart.filename())
        return builder.build()
    }
}
