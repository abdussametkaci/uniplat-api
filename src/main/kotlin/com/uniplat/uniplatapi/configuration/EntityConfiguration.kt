package com.uniplat.uniplatapi.configuration

import com.uniplat.uniplatapi.component.dsl.EntityProperties
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.context.annotation.Configuration
import org.springframework.core.type.filter.AnnotationTypeFilter
import org.springframework.data.relational.core.mapping.Table
import kotlin.reflect.KClass

@Configuration
class EntityConfiguration {

    val entityProperties = mutableMapOf<KClass<*>, EntityProperties>()

    @PostConstruct
    fun generateEntityProperties() {
        val provider = ClassPathScanningCandidateComponentProvider(false)
        provider.addIncludeFilter(AnnotationTypeFilter(Table::class.java))
        val entities = provider.findCandidateComponents("com.uniplat.uniplatapi.domain.model")
        entities.forEach {
            val clazz = Class.forName(it.beanClassName).kotlin
            entityProperties[clazz] = EntityProperties(tableName(clazz), fields(clazz))
        }
    }

    fun getEntityProperties(clazz: KClass<*>): EntityProperties {
        return entityProperties[clazz]!!
    }
}

fun tableName(clazz: KClass<*>): String {
    return with(clazz) { (annotations.find { it is Table } as? Table)?.value?.takeIf { it.isNotEmpty() } ?: simpleName?.toLowerUnderScore() ?: "" }
}

fun fields(clazz: KClass<*>): List<String> {
    return with(clazz) { java.declaredFields.map { it.name } }
}

fun String.toLowerUnderScore(): String {
    var output = ""
    this.forEachIndexed { i, char ->
        output += if (char.isUpperCase() && i > 0) "_${char.lowercase()}" else char.lowercase()
    }
    return output
}
