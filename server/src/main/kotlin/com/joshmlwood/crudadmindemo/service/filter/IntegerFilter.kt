package com.joshmlwood.crudadmindemo.service.filter

class IntegerFilter(
    greaterThan: Int? = null,
    lessThan: Int? = null,
    greaterThanOrEqualTo: Int? = null,
    lessThanOrEqualTo: Int? = null,
    equals: Int? = null,
    specified: Boolean? = null,
    `in`: List<Int>? = null
) : RangeFilter<Int>(
    greaterThan = greaterThan,
    lessThan = lessThan,
    greaterThanOrEqualTo = greaterThanOrEqualTo,
    lessThanOrEqualTo = lessThanOrEqualTo,
    equals = equals,
    specified = specified,
    `in` = `in`
)
