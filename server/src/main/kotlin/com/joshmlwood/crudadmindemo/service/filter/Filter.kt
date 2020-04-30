package com.joshmlwood.crudadmindemo.service.filter

import java.io.Serializable
import java.util.Objects

open class Filter<FIELD_TYPE>(
    val equals: FIELD_TYPE? = null,
    val specified: Boolean? = null,
    val `in`: List<FIELD_TYPE>? = null
) : Serializable
