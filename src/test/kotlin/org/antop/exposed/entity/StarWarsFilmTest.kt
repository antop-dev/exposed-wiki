package org.antop.exposed.entity

import org.antop.exposed.exposed
import org.antop.exposed.table.StarWarsFilms
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.SortOrder
import org.junit.jupiter.api.Test

class StarWarsFilmTest {
    @Test
    fun create() {
        exposed(StarWarsFilms) {
            // INSERT INTO STARWARSFILMS (DIRECTOR, "NAME", SEQUEL_ID) VALUES ('Rian Johnson', 'The Last Jedi', 8)
            val movie = StarWarsFilm.new {
                name = "The Last Jedi"
                sequelId = 8
                director = "Rian Johnson"
            }
            println("movie = $movie")

            // id는 EntityID<Int> 타입니다.
            // 직접 꺼내서 사용하기 전까지 인서트되지 않고 트랜잭션이 끝날 때 인서트 된다.
            val id = movie.id
            // 만약 직접 꺼내 써야한다면 이 때 인서트 된다.
            // 로그 순서 확인할 것
            // println("move id = ${id.value}")
        }
    }

    @Test
    fun read() {
        exposed(StarWarsFilms) {
            // SELECT STARWARSFILMS.ID, STARWARSFILMS.SEQUEL_ID, STARWARSFILMS."NAME", STARWARSFILMS.DIRECTOR FROM STARWARSFILMS WHERE STARWARSFILMS.SEQUEL_ID = 8
            val movies: SizedIterable<StarWarsFilm> = StarWarsFilm.find { StarWarsFilms.sequelId eq 8 }
            movies.forEach { println(it) }
            // SELECT STARWARSFILMS.ID, STARWARSFILMS.SEQUEL_ID, STARWARSFILMS."NAME", STARWARSFILMS.DIRECTOR FROM STARWARSFILMS WHERE STARWARSFILMS.ID = 5
            val movie: StarWarsFilm? = StarWarsFilm.findById(5)
            println("movie = $movie")
        }
    }

    @Test
    fun sort() {
        exposed(StarWarsFilms) {
            // SELECT STARWARSFILMS.ID, STARWARSFILMS.SEQUEL_ID, STARWARSFILMS."NAME", STARWARSFILMS.DIRECTOR
            // FROM STARWARSFILMS
            // ORDER BY STARWARSFILMS.SEQUEL_ID ASC
            val movies = StarWarsFilm.all()
                // 이 정렬은 SQL로 정렬하는 것이 아닌 것 같은데...
                // .sortedByDescending{ it.sequelId }
                .orderBy(StarWarsFilms.sequelId to SortOrder.ASC)
            movies.forEach { println(it) }
        }
    }

    @Test
    fun update() {
        exposed(StarWarsFilms) {
            StarWarsFilm.new {
                name = "The Last Jedi"
                sequelId = 8
                director = "Rian Johnson"
            }
            // SELECT STARWARSFILMS.ID, STARWARSFILMS.SEQUEL_ID, STARWARSFILMS."NAME", STARWARSFILMS.DIRECTOR
            // FROM STARWARSFILMS WHERE STARWARSFILMS.ID = 1
            val movie = StarWarsFilm.findById(1)
            println("movie = $movie")
            // 다시 조회하지 않는다.
            val other = StarWarsFilm.findById(1)
            println("other = $other")
            movie?.let {
                it.name = "Episode VIII – The Last Jedi"
                it.director = "Antop"
            }
            println("movie = $movie")
        }
        // 트랜잭션이 끝나는 시점
        // UPDATE STARWARSFILMS SET DIRECTOR='Antop', "NAME"='Episode VIII – The Last Jedi' WHERE ID = 1
    }
}
