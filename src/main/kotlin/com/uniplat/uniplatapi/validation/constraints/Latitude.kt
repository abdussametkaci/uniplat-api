package com.uniplat.uniplatapi.validation.constraints

import com.uniplat.uniplatapi.validation.constraintvalidator.LatitudeValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [LatitudeValidator::class])
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Latitude(
    val message: String = "Invalid latitude",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
