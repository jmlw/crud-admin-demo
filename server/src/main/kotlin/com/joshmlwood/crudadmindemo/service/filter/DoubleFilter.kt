package com.joshmlwood.crudadmindemo.service.filter

import java.math.BigDecimal

class DoubleFilter(
    greaterThan: Double? = null,
    lessThan: Double? = null,
    greaterThanOrEqualTo: Double? = null,
    lessThanOrEqualTo: Double? = null,
    equals: Double? = null,
    specified: Boolean? = null,
    `in`: List<Double>? = null
) : RangeFilter<Double>(
    greaterThan = greaterThan,
    lessThan = lessThan,
    greaterThanOrEqualTo = greaterThanOrEqualTo,
    lessThanOrEqualTo = lessThanOrEqualTo,
    equals = equals,
    specified = specified,
    `in` = `in`
)
