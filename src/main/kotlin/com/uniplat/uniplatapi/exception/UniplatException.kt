package com.uniplat.uniplatapi.exception

import org.springframework.http.HttpStatus

class UniplatException(
    messageProperty: String,
    args: List<Any> = emptyList(),
    errors: List<Error> = emptyList()
) : BaseResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", messageProperty, args, errors)
