package org.antop.exposed.entity

import org.antop.exposed.table.Actors
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Actor(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Actor>(Actors)

    var firstname by Actors.firstname
    var lastname by Actors.lastname
}
