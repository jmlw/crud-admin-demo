package com.joshmlwood.crudadmindemo.service.filter

import java.time.Instant
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.format.annotation.DateTimeFormat.ISO

class InstantFilter(
    greaterThan: Instant? = null,
    lessThan: Instant? = null,
    greaterThanOrEqualTo: Instant? = null,
    lessThanOrEqualTo: Instant? = null,
    equals: Instant? = null,
    specified: Boolean? = null,
    `in`: List<Instant>? = null
) : RangeFilter<Instant>(
    greaterThan = greaterThan,
    lessThan = lessThan,
    greaterThanOrEqualTo = greaterThanOrEqualTo,
    lessThanOrEqualTo = lessThanOrEqualTo,
    equals = equals,
    specified = specified,
    `in` = `in`
)
