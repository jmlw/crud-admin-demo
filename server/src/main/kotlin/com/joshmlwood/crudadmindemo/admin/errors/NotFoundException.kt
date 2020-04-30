package com.joshmlwood.crudadmindemo.admin.errors

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import kotlin.reflect.KClass

@ResponseStatus(HttpStatus.NOT_FOUND)
class EntityNotFoundException(private val clazz: KClass<*>, private val id: Any?) : RuntimeException("${clazz.simpleName} with id [$id] not found")
