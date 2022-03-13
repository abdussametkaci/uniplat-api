package com.uniplat.uniplatapi.exception

data class Error(
    val code: String,
    val messageProperty: String? = null,
    val messageArgs: List<Any> = emptyList(),
    val message: String? = null
)
