package com.uniplat.uniplatapi.exception

import org.springframework.http.HttpStatus

class UnauthorizedException(
    messageProperty: String,
    args: List<Any> = emptyList(),
    errors: List<Error> = emptyList()
) : BaseResponseStatusException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", messageProperty, args, errors)
