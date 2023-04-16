package org.antop.exposed.entity

import org.antop.exposed.table.UserRatings
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserRating(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserRating>(UserRatings)

    var value by UserRatings.value
    var film by StarWarsFilm referencedOn UserRatings.film // use referencedOn for normal references
    var user by User referencedOn UserRatings.user
    var secondUser by User optionalReferencedOn UserRatings.secondUser

}
