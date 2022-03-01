package com.uniplat.uniplatapi.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class UniplatException(override val message: String, args: Array<Any>? = null) : ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, Messages.getMessage(message, args)) {
    init {
        error(reason!!)
    }
}
