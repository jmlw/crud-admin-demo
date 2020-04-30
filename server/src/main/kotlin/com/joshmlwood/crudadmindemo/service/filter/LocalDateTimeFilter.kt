package com.joshmlwood.crudadmindemo.service.filter

import java.time.LocalDateTime

class LocalDateTimeFilter(
    greaterThan: LocalDateTime? = null,
    lessThan: LocalDateTime? = null,
    greaterThanOrEqualTo: LocalDateTime? = null,
    lessThanOrEqualTo: LocalDateTime? = null,
    equals: LocalDateTime? = null,
    specified: Boolean? = null,
    `in`: List<LocalDateTime>? = null
) : RangeFilter<LocalDateTime>(
    greaterThan = greaterThan,
    lessThan = lessThan,
    greaterThanOrEqualTo = greaterThanOrEqualTo,
    lessThanOrEqualTo = lessThanOrEqualTo,
    equals = equals,
    specified = specified,
    `in` = `in`
)
