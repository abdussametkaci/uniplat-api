package com.uniplat.uniplatapi.component

import kotlinx.coroutines.withContext
import org.springframework.core.convert.TypeDescriptor
import org.springframework.core.convert.converter.Converter
import org.springframework.core.convert.converter.GenericConverter
import org.springframework.core.convert.support.GenericConversionService
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.reflect.full.callSuspendBy

@Suppress("UNCHECKED_CAST")
class ContextAwareConversionService : GenericConversionService() {

    fun registerConverters(converters: Set<Converter<*, *>>): ContextAwareConversionService {
        return this.apply {
            converters.forEach {
                when (it) {
                    is ContextAwareConverter -> addConverter(it as GenericConverter)
                    else -> addConverter(it)
                }
            }
        }
    }

    suspend fun <T> convertSuspend(source: Any, targetType: Class<T>): T {
        val converter = getConverter(TypeDescriptor.forObject(source)!!, TypeDescriptor.valueOf(targetType))
        return if (converter is ContextAwareConverter<*, *>) {
            converter.convert(source)
        } else {
            convert(source, targetType)
        } as T
    }

    private suspend fun ContextAwareConverter<*, *>.convert(source: Any): Any? {
        return this::convertSuspend.run {
            val sourceParam = parameters.first() to source
            callSuspendBy(mapOf(sourceParam))
        }
    }
}

suspend inline fun <reified T> ContextAwareConversionService.convert(source: Any, conversionContext: CoroutineContext = EmptyCoroutineContext): T {
    return withContext(conversionContext) {
        convertSuspend(source, T::class.java)
    }
}
