package org.antop.exposed.entity

import org.antop.exposed.table.StarWarsFilms
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class StarWarsFilm(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<StarWarsFilm>(StarWarsFilms)

    var sequelId by StarWarsFilms.sequelId
    var name by StarWarsFilms.name
    var director by StarWarsFilms.director

    override fun toString(): String {
        return "StarWarsFilm(id=$id, sequelId=$sequelId, name='$name', director='$director')"
    }

}
