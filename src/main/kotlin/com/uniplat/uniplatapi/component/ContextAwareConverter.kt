package com.uniplat.uniplatapi.component

import org.springframework.core.ResolvableType
import org.springframework.core.convert.TypeDescriptor
import org.springframework.core.convert.converter.Converter
import org.springframework.core.convert.converter.GenericConverter

interface ContextAwareConverter<S, T> : GenericConverter, Converter<S, T> {

    @JvmDefault
    suspend fun convertSuspend(source: S): T {
        return convert(source!!)!!
    }

    @JvmDefault
    override fun getConvertibleTypes(): MutableSet<GenericConverter.ConvertiblePair>? {
        val converter = this.javaClass
        val resolvableType = ResolvableType.forClass(converter).`as`(Converter::class.java)
        val source = resolvableType.generics.first().resolve()!!
        val target = resolvableType.generics[1].resolve()!!
        return mutableSetOf(GenericConverter.ConvertiblePair(source, target))
    }

    override fun convert(source: Any?, sourceType: TypeDescriptor, targetType: TypeDescriptor): Any? {
        throw NotImplementedError("Use convertSuspend method")
    }
}
