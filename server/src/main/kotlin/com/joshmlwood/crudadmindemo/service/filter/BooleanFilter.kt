package com.joshmlwood.crudadmindemo.service.filter

import com.joshmlwood.crudadmindemo.service.filter.Filter

class BooleanFilter(
    equals: Boolean? = null,
    specified: Boolean? = null,
    `in`: List<Boolean>? = null
) : Filter<Boolean>(
    equals = equals,
    specified = specified,
    `in` = `in`
)
