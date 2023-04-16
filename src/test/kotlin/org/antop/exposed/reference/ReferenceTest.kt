package org.antop.exposed.reference

import org.antop.exposed.entity.Actor
import org.antop.exposed.entity.StarWarsFilm
import org.antop.exposed.entity.User
import org.antop.exposed.entity.UserRating
import org.antop.exposed.exposed
import org.antop.exposed.table.*
import org.jetbrains.exposed.sql.SizedCollection
import org.junit.jupiter.api.Test

class ReferenceTest {
    @Test
    fun manyToOne() {
        exposed(Users, StarWarsFilms, UserRatings) {
            val movie = StarWarsFilm.new {
                name = "The Last Jedi"
                sequelId = 8
                director = "Rian Johnson"
            }

            val user = User.new {
                name = "Antop"
            }

            // rating을 인서트할 때 movie와 user의 id 값이 필요하다.
            // 그래서 여기서 movie와 user가 실제로 인서트 된다.

            // INSERT INTO USERS ("NAME") VALUES ('Antop')
            // INSERT INTO STARWARSFILMS (DIRECTOR, "NAME", SEQUEL_ID) VALUES ('Rian Johnson', 'The Last Jedi', 8)
            val rating = UserRating.new {
                film = movie
                this.user = user
                value = 8
            }

            println("rating = ${rating.value}")
            // 아직 rating은 insert 되지 않았다.
            // 만약 아래에서 조회를 하지 않았다면 트랜잭션이 끝나는 시점에 인서트 된다.

            println("antop's rating count = ${user.ratings.count()}") // SELECT COUNT(*) FROM USERRATINGS WHERE USERRATINGS."USER" = 1
            println("antop's rating count = ${user.ratings.count()}") // SELECT COUNT(*) FROM USERRATINGS WHERE USERRATINGS."USER" = 1

            // SELECT USERRATINGS.ID, USERRATINGS."VALUE", USERRATINGS.FILM, USERRATINGS."USER", USERRATINGS.SECOND_USER FROM USERRATINGS WHERE USERRATINGS."USER" = 1
            for (r in user.ratings) { // 목록을 조회 했다.
                println("r = $r")
            }

            // 위에서 목록을 가져온 후 count() 함수를 다시 호출하면 카운트 쿼리가 실행되지 않음
            println("antop's rating count = ${user.ratings.count()}")
        }
    }

    @Test
    fun manyToMany() {
        /*
         * 물리적으로는
         * - StarWarsFilms 1:N StarWarsFilmActors
         * - StarWarsFilmActors N:1 Actors
         *
         * 사용할 때는
         * - StarWarsFilm N:M Actor
         */
        //
        exposed(StarWarsFilms, StarWarsFilmActors, Actors) {
            // INSERT INTO STARWARSFILMS (DIRECTOR, "NAME", SEQUEL_ID) VALUES ('Rian Johnson', 'The Last Jedi', 8)
            val film = StarWarsFilm.new {
                name = "The Last Jedi"
                sequelId = 8
                director = "Rian Johnson"
            }
            val actor1 = Actor.new {
                firstname = "Daisy"
                lastname = "Ridley"
            }
            val actor2 = Actor.new {
                firstname = "Daisy"
                lastname = "Ridley"
            }

            film.actors = SizedCollection(actor1, actor2)
            // INSERT INTO STARWARSFILMS (DIRECTOR, "NAME", SEQUEL_ID) VALUES ('Rian Johnson', 'The Last Jedi', 8)
            // INSERT INTO ACTORS (FIRSTNAME, LASTNAME) VALUES ('Daisy', 'Ridley')
            // INSERT INTO ACTORS (FIRSTNAME, LASTNAME) VALUES ('Daisy', 'Ridley')
            // SELECT ACTORS.ID, ACTORS.FIRSTNAME, ACTORS.LASTNAME, STARWARSFILMACTORS.ACTOR, STARWARSFILMACTORS."starWarsFilm" FROM ACTORS INNER JOIN STARWARSFILMACTORS ON ACTORS.ID = STARWARSFILMACTORS.ACTOR WHERE STARWARSFILMACTORS."starWarsFilm" = 1
            // DELETE FROM STARWARSFILMACTORS WHERE (STARWARSFILMACTORS."starWarsFilm" = 1) AND (STARWARSFILMACTORS.ACTOR NOT IN (1, 2))
            // INSERT INTO STARWARSFILMACTORS (ACTOR, "starWarsFilm") VALUES (1, 1)
            // INSERT INTO STARWARSFILMACTORS (ACTOR, "starWarsFilm") VALUES (2, 1)
            // 트랜잭션이 끝낼때 인서트 되는 게 아니다.

            println("${film.name} actors is ${film.actors.joinToString(", ") { "${it.lastname} ${it.firstname}" }}")
        }
    }
}
