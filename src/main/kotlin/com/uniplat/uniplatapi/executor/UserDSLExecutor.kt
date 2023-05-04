package com.uniplat.uniplatapi.executor

import com.uniplat.uniplatapi.component.dsl.CoroutineFilteringRepository
import com.uniplat.uniplatapi.component.dsl.DefaultDSLExecutor
import com.uniplat.uniplatapi.component.dsl.SqlSelectBuilder
import com.uniplat.uniplatapi.component.dsl.query
import com.uniplat.uniplatapi.domain.model.User
import org.springframework.stereotype.Component

@Component
class UserDSLExecutor(reactiveFilteringRepository: CoroutineFilteringRepository<User>) : DefaultDSLExecutor<User>(reactiveFilteringRepository) {

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
                                if (param.key == "name") {
                                    param.key ilike "%$it%"
                                } else {
                                    param.key eq it
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
