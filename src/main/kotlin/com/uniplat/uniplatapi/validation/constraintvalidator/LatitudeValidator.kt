package com.uniplat.uniplatapi.validation.constraintvalidator

import com.uniplat.uniplatapi.validation.constraints.Latitude
import java.math.BigDecimal
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class LatitudeValidator : ConstraintValidator<Latitude, BigDecimal> {

    override fun isValid(value: BigDecimal?, context: ConstraintValidatorContext?): Boolean {
        return value?.let {
            BigDecimal.valueOf(-90) <= it && it <= BigDecimal.valueOf(90)
        } ?: true
    }
}
