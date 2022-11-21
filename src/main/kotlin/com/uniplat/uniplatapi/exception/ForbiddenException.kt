package com.uniplat.uniplatapi.exception

import org.springframework.http.HttpStatus

class ForbiddenException(
    messageProperty: String,
    args: List<Any> = emptyList(),
    errors: List<Error> = emptyList()
) : BaseResponseStatusException(HttpStatus.FORBIDDEN, "FORBIDDEN", messageProperty, args, errors)
