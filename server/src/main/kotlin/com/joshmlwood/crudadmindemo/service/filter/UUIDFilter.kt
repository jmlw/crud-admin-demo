package com.joshmlwood.crudadmindemo.service.filter

import com.joshmlwood.crudadmindemo.service.filter.Filter
import java.util.UUID

class UUIDFilter(
    equals: UUID? = null,
    specified: Boolean? = null,
    `in`: List<UUID>? = null
) : Filter<UUID>(
    equals = equals,
    specified = specified,
    `in` = `in`
)
