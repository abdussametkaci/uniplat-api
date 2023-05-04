package com.uniplat.uniplatapi.component.dsl

import io.r2dbc.spi.Row
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.r2dbc.core.awaitOne
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

@Component
class ReactiveFilteringRepository<T : Any>(private val r2dbcEntityTemplate: R2dbcEntityTemplate) : CoroutineFilteringRepository<T> {

    override fun findAll(query: String, type: KClass<T>): Flow<T> {
        return r2dbcEntityTemplate.databaseClient.sql(query)
            .map { row -> r2dbcEntityTemplate.converter.read(type.java, row as Row) }
            .all()
            .asFlow()
    }

    override suspend fun count(query: String): Long {
        return r2dbcEntityTemplate.databaseClient.sql(query)
            .map { row, _ -> mapCount(row) }
            .awaitOne()
            .count
    }

    private fun mapCount(row: Row) = Count(row.get("count", Long::class.javaObjectType)!!)

    data class Count(
        val count: Long
    )
}
