package com.uniplat.uniplatapi.validation.constraints

import com.uniplat.uniplatapi.validation.constraintvalidator.LongitudeValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [LongitudeValidator::class])
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Longitude(
    val message: String = "Invalid longitude",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
