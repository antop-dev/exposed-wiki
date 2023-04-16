package org.antop.exposed.table

import org.jetbrains.exposed.sql.Table

object StarWarsFilmActors : Table() {
    val starWarsFilm = reference("starWarsFilm", StarWarsFilms)
    val actor = reference("actor", Actors)
    override val primaryKey = PrimaryKey(
        starWarsFilm,
        actor,
        name = "PK_StarWarsFilmActors_swf_act"
    ) // PK_StarWarsFilmActors_swf_act is optional here
}
