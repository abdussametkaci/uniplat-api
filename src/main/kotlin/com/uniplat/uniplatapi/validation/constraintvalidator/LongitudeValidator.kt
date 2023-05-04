package com.uniplat.uniplatapi.validation.constraintvalidator

import com.uniplat.uniplatapi.validation.constraints.Longitude
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.math.BigDecimal

class LongitudeValidator : ConstraintValidator<Longitude, BigDecimal> {

    override fun isValid(value: BigDecimal?, context: ConstraintValidatorContext?): Boolean {
        return value?.let {
            BigDecimal.valueOf(-180) <= it && it <= BigDecimal.valueOf(180)
        } ?: true
    }
}
