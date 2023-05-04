package com.uniplat.uniplatapi.component.dsl

import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

interface CoroutineFilteringRepository<T : Any> {

    fun findAll(query: String, type: KClass<T>): Flow<T>

    suspend fun count(query: String): Long
}

inline fun <reified T : Any> CoroutineFilteringRepository<T>.findAll(query: String): Flow<T> {
    return findAll(query, T::class)
}
