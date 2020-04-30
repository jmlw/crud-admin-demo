package com.joshmlwood.crudadmindemo.service.filter

class FloatFilter(
    greaterThan: Float? = null,
    lessThan: Float? = null,
    greaterThanOrEqualTo: Float? = null,
    lessThanOrEqualTo: Float? = null,
    equals: Float? = null,
    specified: Boolean? = null,
    `in`: List<Float>? = null
) : RangeFilter<Float>(
    greaterThan = greaterThan,
    lessThan = lessThan,
    greaterThanOrEqualTo = greaterThanOrEqualTo,
    lessThanOrEqualTo = lessThanOrEqualTo,
    equals = equals,
    specified = specified,
    `in` = `in`
)
