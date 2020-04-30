package com.joshmlwood.crudadmindemo.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotNull

@Entity
class Feature(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0L,

    var name: String = ""
)
