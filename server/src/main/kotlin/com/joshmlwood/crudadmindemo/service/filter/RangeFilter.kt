package com.joshmlwood.crudadmindemo.service.filter

import com.joshmlwood.crudadmindemo.service.filter.Filter
import java.util.Objects

open class RangeFilter<FIELD_TYPE : Comparable<FIELD_TYPE>>(
    val greaterThan: FIELD_TYPE? = null,
    val lessThan: FIELD_TYPE? = null,
    val greaterThanOrEqualTo: FIELD_TYPE? = null,
    val lessThanOrEqualTo: FIELD_TYPE? = null,
    equals: FIELD_TYPE? = null,
    specified: Boolean? = null,
    `in`: List<FIELD_TYPE>? = null
) : Filter<FIELD_TYPE>(
    equals = equals,
    specified = specified,
    `in` = `in`
)
