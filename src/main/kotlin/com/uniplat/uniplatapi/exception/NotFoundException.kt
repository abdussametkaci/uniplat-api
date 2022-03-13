package com.uniplat.uniplatapi.exception

import org.springframework.http.HttpStatus

class NotFoundException(
    messageProperty: String,
    args: List<Any> = emptyList(),
    errors: List<Error> = emptyList()
) : BaseResponseStatusException(HttpStatus.NOT_FOUND, "NOT_FOUND", messageProperty, args, errors)
