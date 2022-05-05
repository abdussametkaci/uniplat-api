package com.uniplat.uniplatapi.controller

import com.uniplat.uniplatapi.extensions.withUserId
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class GetRequestController {

    @PostMapping("/request")
    suspend fun getRequest(): UUID {
        return withUserId { userId ->
            userId
        }
    }
}
