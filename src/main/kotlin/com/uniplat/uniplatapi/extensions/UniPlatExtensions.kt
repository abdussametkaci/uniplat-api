package com.uniplat.uniplatapi.extensions

import org.springframework.data.r2dbc.convert.EnumWriteSupport

inline fun <reified T : Enum<T>?> enumConverterOf(): EnumWriteSupport<T> {
    return object : EnumWriteSupport<T>() {}
}
