package org.antop.exposed.table

import org.jetbrains.exposed.dao.id.IntIdTable

object UserRatings : IntIdTable() {
    val value = long("value")
    val film = reference("film", StarWarsFilms)
    val user = reference("user", Users)
    val secondUser = reference("second_user", Users).nullable()
}
