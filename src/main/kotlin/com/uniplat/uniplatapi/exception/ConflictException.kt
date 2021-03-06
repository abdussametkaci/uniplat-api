package com.uniplat.uniplatapi.exception

import org.springframework.http.HttpStatus

class ConflictException(
    messageProperty: String,
    args: List<Any> = emptyList(),
    errors: List<Error> = emptyList()
) : BaseResponseStatusException(HttpStatus.CONFLICT, "CONFLICT", messageProperty, args, errors)
