package com.uniplat.uniplatapi.exception

import org.springframework.http.HttpStatus

class OptimisticLockException : BaseResponseStatusException(HttpStatus.CONFLICT, "OPTIMISTIC_LOCK", messageProperty = "error.*.optimistic-lock")
