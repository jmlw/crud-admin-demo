package com.joshmlwood.crudadmindemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CrudAdminDemoApplication

fun main(args: Array<String>) {
	runApplication<CrudAdminDemoApplication>(*args)
}
