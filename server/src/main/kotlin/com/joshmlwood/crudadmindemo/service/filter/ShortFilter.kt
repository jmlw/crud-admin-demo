package com.joshmlwood.crudadmindemo.service.filter

class ShortFilter(
    greaterThan: Short? = null,
    lessThan: Short? = null,
    greaterThanOrEqualTo: Short? = null,
    lessThanOrEqualTo: Short? = null,
    equals: Short? = null,
    specified: Boolean? = null,
    `in`: List<Short>? = null
) : RangeFilter<Short>(
    greaterThan = greaterThan,
    lessThan = lessThan,
    greaterThanOrEqualTo = greaterThanOrEqualTo,
    lessThanOrEqualTo = lessThanOrEqualTo,
    equals = equals,
    specified = specified,
    `in` = `in`
)
