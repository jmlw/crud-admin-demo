package com.joshmlwood.crudadmindemo.service.filter

import java.math.BigDecimal

class BigDecimalFilter(
    greaterThan: BigDecimal? = null,
    lessThan: BigDecimal? = null,
    greaterThanOrEqualTo: BigDecimal? = null,
    lessThanOrEqualTo: BigDecimal? = null,
    equals: BigDecimal? = null,
    specified: Boolean? = null,
    `in`: List<BigDecimal>? = null
) : RangeFilter<BigDecimal>(
    greaterThan = greaterThan,
    lessThan = lessThan,
    greaterThanOrEqualTo = greaterThanOrEqualTo,
    lessThanOrEqualTo = lessThanOrEqualTo,
    equals = equals,
    specified = specified,
    `in` = `in`
)
