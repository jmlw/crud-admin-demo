package com.joshmlwood.crudadmindemo.service.dto

import com.joshmlwood.crudadmindemo.admin.Cardinality
import com.joshmlwood.crudadmindemo.admin.PrimaryKey
import com.joshmlwood.crudadmindemo.admin.Relation

data class AdminRoomDTO(
    @PrimaryKey
    var id: Long = 0L,
    var floor: Int = 1,
    var occupancy: Int = 1,
    var description: String = "",
    @Relation("features", Cardinality.TO_MANY)
    var features: Set<AdminAutocompleteDTO> = emptySet()
)
