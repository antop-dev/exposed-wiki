package org.antop.exposed.entity

import org.antop.exposed.table.StarWarsFilmActors
import org.antop.exposed.table.StarWarsFilms
import org.antop.exposed.table.UserRatings
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable

class StarWarsFilm(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<StarWarsFilm>(StarWarsFilms)

    var sequelId by StarWarsFilms.sequelId
    var name by StarWarsFilms.name
    var director by StarWarsFilms.director

    val ratings by UserRating referrersOn UserRatings.film // make sure to use val and referrersOn
    var actors: SizedIterable<Actor> by Actor via StarWarsFilmActors

}
