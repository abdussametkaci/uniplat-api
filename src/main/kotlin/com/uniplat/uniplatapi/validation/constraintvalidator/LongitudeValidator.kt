package com.uniplat.uniplatapi.validation.constraintvalidator

import com.uniplat.uniplatapi.validation.constraints.Longitude
import java.math.BigDecimal
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class LongitudeValidator : ConstraintValidator<Longitude, BigDecimal> {

    override fun isValid(value: BigDecimal?, context: ConstraintValidatorContext?): Boolean {
        return value?.let {
            BigDecimal.valueOf(-180) <= it && it <= BigDecimal.valueOf(180)
        } ?: true
    }
}
