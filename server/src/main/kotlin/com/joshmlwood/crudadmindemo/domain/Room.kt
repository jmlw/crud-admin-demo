package com.joshmlwood.crudadmindemo.domain

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany

@Entity
class Room(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0L,

    var floor: Int = 1,
    var occupancy: Int = 1,
    var description: String = "",

    @ManyToMany
    @JoinTable(
        name = "room_features",
        joinColumns = [JoinColumn(name = "room_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(
            name = "feature_id",
            referencedColumnName = "id"
        )]
    )
    var features: Set<Feature> = emptySet()
)
