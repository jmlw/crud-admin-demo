package com.joshmlwood.crudadmindemo.service.dto

import com.joshmlwood.crudadmindemo.admin.Cardinality
import com.joshmlwood.crudadmindemo.admin.PrimaryKey
import com.joshmlwood.crudadmindemo.admin.Relation

data class AdminHotelDTO(
    @PrimaryKey
    var id: Long = 0L,
    var name: String = "",
    var address: String = "",
    @Relation("rooms", Cardinality.TO_MANY)
    var rooms: List<AdminAutocompleteDTO> = emptyList()
)
