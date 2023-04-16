package org.antop.exposed.entity

import org.antop.exposed.table.UserRatings
import org.antop.exposed.table.Users
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)

    var name by Users.name
    val ratings by UserRating referrersOn UserRatings.user

}
