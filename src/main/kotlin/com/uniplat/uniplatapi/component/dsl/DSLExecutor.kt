package com.uniplat.uniplatapi.component.dsl

import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

interface DSLExecutor<T : Any> {

    fun query(): SqlSelectBuilder

    fun findAll(type: KClass<T>): Flow<T>

    suspend fun count(): Long
}
