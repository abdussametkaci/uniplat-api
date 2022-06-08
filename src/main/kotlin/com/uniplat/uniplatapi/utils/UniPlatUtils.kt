package com.uniplat.uniplatapi.utils

import org.springframework.http.server.reactive.ServerHttpRequest

suspend fun getURL(serverHttpRequest: ServerHttpRequest): String {
    val uri = serverHttpRequest.uri
    return """${uri.scheme}://${uri.host}:${uri.port}"""
}
