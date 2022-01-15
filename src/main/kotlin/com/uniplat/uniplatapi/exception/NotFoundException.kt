package com.uniplat.uniplatapi.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class NotFoundException(override val message: String, args: Array<Any>? = null) : ResponseStatusException(HttpStatus.NOT_FOUND, Messages.getMessage(message, args)) {
    init {
        error(reason!!)
    }
}
