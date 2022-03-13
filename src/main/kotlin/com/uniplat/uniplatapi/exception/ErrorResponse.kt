package com.uniplat.uniplatapi.exception

data class ErrorResponse(
    val code: String,
    val message: String,
    val errors: List<SubError>
)

data class SubError(
    val code: String,
    val message: String
)
