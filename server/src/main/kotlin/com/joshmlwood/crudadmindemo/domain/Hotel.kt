package com.joshmlwood.crudadmindemo.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class Hotel(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0L,

    var name: String = "",
    var address: String = "",

    @OneToMany
    var rooms: List<Room> = emptyList()
)
