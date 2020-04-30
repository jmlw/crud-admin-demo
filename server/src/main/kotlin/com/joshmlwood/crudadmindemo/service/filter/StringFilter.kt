package com.joshmlwood.crudadmindemo.service.filter

import com.joshmlwood.crudadmindemo.service.filter.Filter
import java.util.Objects

class StringFilter(
    val contains: String? = null,
    equals: String? = null,
    specified: Boolean? = null,
    `in`: List<String>? = null
) : Filter<String>(
    equals = equals,
    specified = specified,
    `in` = `in`
)
