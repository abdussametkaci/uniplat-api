package com.uniplat.uniplatapi.component.dsl

import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Component
import org.springframework.util.MultiValueMap
import kotlin.reflect.KClass

@Component
class DefaultDSLExecutor<T : Any>(
    private val reactiveFilteringRepository: CoroutineFilteringRepository<T>
) : DSLExecutor<T> {

    lateinit var entityProperties: EntityProperties
    lateinit var parameters: MultiValueMap<String, String>

    // Must be called first, the use other methods
    suspend fun setEntityPropertiesWithParameters(entityProperties: EntityProperties, parameters: MultiValueMap<String, String>) {
        this.entityProperties = entityProperties
        this.parameters = parameters
    }

    override fun query(): SqlSelectBuilder {
        return query {
            select("*")
            from(entityProperties.tableName)
            where {
                and {
                    parameters.onEach { param ->
                        if (!entityProperties.fields.contains(param.key)) return@onEach
                        or {
                            param.value.onEach {
                                param.key eq it
                            }
                        }
                    }
                }
            }
        }
    }

    override fun findAll(type: KClass<T>): Flow<T> {
        return reactiveFilteringRepository.findAll(query().toString(), type)
    }

    override suspend fun count(): Long {
        return reactiveFilteringRepository.count(query().toCountQuery())
    }
}

inline fun <reified T : Any> DefaultDSLExecutor<T>.findAll(): Flow<T> {
    return findAll(T::class)
}
