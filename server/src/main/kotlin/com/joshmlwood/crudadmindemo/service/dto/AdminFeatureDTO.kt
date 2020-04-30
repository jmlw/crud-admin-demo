package com.joshmlwood.crudadmindemo.service.dto

import com.joshmlwood.crudadmindemo.admin.PrimaryKey

data class AdminFeatureDTO(
    @PrimaryKey
    var id: Long = 0L,
    var name: String = ""
)

