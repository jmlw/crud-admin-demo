package com.joshmlwood.crudadmindemo.service.filter

import java.time.LocalDate
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.format.annotation.DateTimeFormat.ISO

class LocalDateFilter(
    greaterThan: LocalDate? = null,
    lessThan: LocalDate? = null,
    greaterThanOrEqualTo: LocalDate? = null,
    lessThanOrEqualTo: LocalDate? = null,
    equals: LocalDate? = null,
    specified: Boolean? = null,
    `in`: List<LocalDate>? = null
) : RangeFilter<LocalDate>(
    greaterThan = greaterThan,
    lessThan = lessThan,
    greaterThanOrEqualTo = greaterThanOrEqualTo,
    lessThanOrEqualTo = lessThanOrEqualTo,
    equals = equals,
    specified = specified,
    `in` = `in`
)
