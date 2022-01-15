package com.uniplat.uniplatapi.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class ConflictException(override val message: String, args: Array<Any>? = null) : ResponseStatusException(HttpStatus.CONFLICT, Messages.getMessage(message, args)) {
    init {
        error(reason!!)
    }
}
