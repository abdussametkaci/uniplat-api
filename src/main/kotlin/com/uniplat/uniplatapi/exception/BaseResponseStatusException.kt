package com.uniplat.uniplatapi.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

open class BaseResponseStatusException(
    status: HttpStatus,
    val code: String,
    var messageProperty: String? = null,
    var messageArgs: List<Any> = emptyList(),
    var errors: List<Error> = emptyList(),
) : ResponseStatusException(status)
