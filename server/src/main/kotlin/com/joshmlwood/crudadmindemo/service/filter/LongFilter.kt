package com.joshmlwood.crudadmindemo.service.filter

class LongFilter(
    greaterThan: Long? = null,
    lessThan: Long? = null,
    greaterThanOrEqualTo: Long? = null,
    lessThanOrEqualTo: Long? = null,
    equals: Long? = null,
    specified: Boolean? = null,
    `in`: List<Long>? = null
) : RangeFilter<Long>(
    greaterThan = greaterThan,
    lessThan = lessThan,
    greaterThanOrEqualTo = greaterThanOrEqualTo,
    lessThanOrEqualTo = lessThanOrEqualTo,
    equals = equals,
    specified = specified,
    `in` = `in`
)
