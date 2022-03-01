package com.uniplat.uniplatapi.service

import com.uniplat.uniplatapi.client.file.FileAPI
import com.uniplat.uniplatapi.client.file.dto.response.FileResponse
import org.springframework.core.io.Resource
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class FileService(private val fileAPI: FileAPI) {

    suspend fun upload(filePart: FilePart): FileResponse {
        return fileAPI.upload(filePart)
    }

    suspend fun download(id: UUID): Resource {
        return fileAPI.download(id)
    }

    suspend fun delete(id: UUID) {
        fileAPI.delete(id)
    }
}
