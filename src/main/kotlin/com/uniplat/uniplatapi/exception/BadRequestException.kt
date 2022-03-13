package com.uniplat.uniplatapi.exception

import org.springframework.http.HttpStatus

class BadRequestException(
    messageProperty: String,
    args: List<Any> = emptyList(),
    errors: List<Error> = emptyList()
) : BaseResponseStatusException(HttpStatus.BAD_REQUEST, "BAD_REQUEST", messageProperty, args, errors)
