package org.antop.exposed.table

import org.antop.exposed.exposed
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class StarWarsFilmsTest {

    @Test
    fun create() = exposed(StarWarsFilms) {
        // INSERT INTO STARWARSFILMS (DIRECTOR, "NAME", SEQUEL_ID) VALUES ('Rian Johnson', 'The Last Jedi', 8)
        val id = StarWarsFilms.insertAndGetId {
            it[name] = "The Last Jedi"
            it[sequelId] = 8
            it[director] = "Rian Johnson"
        }
        assertEquals(id.value, 1)
    }

    @Test
    fun read() = exposed(StarWarsFilms) {
        // INSERT INTO STARWARSFILMS (DIRECTOR, "NAME", SEQUEL_ID) VALUES ('Rian Johnson', 'The Last Jedi', 8)
        StarWarsFilms.insert {
            it[name] = "The Last Jedi"
            it[sequelId] = 8
            it[director] = "Rian Johnson"
        }
        // SELECT STARWARSFILMS.ID, STARWARSFILMS.SEQUEL_ID, STARWARSFILMS."NAME", STARWARSFILMS.DIRECTOR FROM STARWARSFILMS WHERE STARWARSFILMS.SEQUEL_ID = 8
        StarWarsFilms.select { StarWarsFilms.sequelId eq 8 }.forEach {
            println(it[StarWarsFilms.name])
        }
        // SELECT STARWARSFILMS."NAME", STARWARSFILMS.DIRECTOR FROM STARWARSFILMS
        val filmAndDirector = StarWarsFilms
            .slice(StarWarsFilms.name, StarWarsFilms.director)
            .selectAll()
            .map {
                it[StarWarsFilms.name] to it[StarWarsFilms.director]
            }
        println("filmAndDirector = $filmAndDirector")
        // SELECT DISTINCT STARWARSFILMS.DIRECTOR FROM STARWARSFILMS WHERE STARWARSFILMS.SEQUEL_ID < 5
        val directors = StarWarsFilms
            .slice(StarWarsFilms.director)
            .select { StarWarsFilms.sequelId less 5 }
            .withDistinct()
            .map {
                it[StarWarsFilms.director]
            }
        println("directors = $directors")
    }

    @Test
    fun update() {
        exposed(StarWarsFilms) {
            // UPDATE STARWARSFILMS SET "NAME"='Episode VIII – The Last Jedi' WHERE STARWARSFILMS.SEQUEL_ID = 8
            StarWarsFilms.update({ StarWarsFilms.sequelId eq 8 }) {
                it[StarWarsFilms.name] = "Episode VIII – The Last Jedi"
            }
            // UPDATE STARWARSFILMS SET SEQUEL_ID=(STARWARSFILMS.SEQUEL_ID + 1) WHERE STARWARSFILMS.SEQUEL_ID = 8
            StarWarsFilms.update({ StarWarsFilms.sequelId eq 8 }) {
                with(SqlExpressionBuilder) {
                    // it.update(sequelId, sequelId + 1)
                    it[sequelId] = sequelId + 1
                }
            }

        }
    }

    @Test
    fun delete() {
        exposed(StarWarsFilms) {
            // DELETE FROM STARWARSFILMS WHERE STARWARSFILMS.SEQUEL_ID = 8
            StarWarsFilms.deleteWhere { sequelId eq 8 }
        }
    }

    @Test
    fun where() {
        exposed(StarWarsFilms, Players) {
            // SELECT STARWARSFILMS.ID, STARWARSFILMS.SEQUEL_ID, STARWARSFILMS."NAME", STARWARSFILMS.DIRECTOR, PLAYERS.SEQUEL_ID, PLAYERS."NAME"
            // FROM STARWARSFILMS
            // INNER JOIN PLAYERS ON STARWARSFILMS.SEQUEL_ID = PLAYERS.SEQUEL_ID
            // WHERE (STARWARSFILMS.DIRECTOR = 'director name') AND (PLAYERS."NAME" = 'player name')
            val query = query(directorName = "director name", playerName = "player name")
            query.forEach { println(it) }
        }
    }

    private fun query(directorName: String? = null, sequelId: Int? = null, playerName: String? = null): Query {
        val query = StarWarsFilms.selectAll()
        // add condition
        directorName?.let { query.andWhere { StarWarsFilms.director eq it } }
        sequelId?.let { query.andWhere { StarWarsFilms.sequelId eq it } }
        // join
        playerName?.let {
            query.adjustColumnSet { innerJoin(Players, { StarWarsFilms.sequelId }, { Players.sequelId }) }
                .adjustSlice { slice(fields + Players.columns) }
                .andWhere { Players.name eq playerName }
        }
        return query
    }

    @Test
    fun count() {
        exposed(StarWarsFilms) {
            // SELECT COUNT(*) FROM STARWARSFILMS WHERE STARWARSFILMS.SEQUEL_ID = 8
            val count = StarWarsFilms.select { StarWarsFilms.sequelId eq 8 }.count()
            println("count = $count")
        }
    }

    @Test
    fun orderBy() {
        exposed(StarWarsFilms) {
            // SELECT STARWARSFILMS.ID, STARWARSFILMS.SEQUEL_ID, STARWARSFILMS."NAME", STARWARSFILMS.DIRECTOR
            // FROM STARWARSFILMS
            // ORDER BY STARWARSFILMS.SEQUEL_ID ASC
            val query = StarWarsFilms.selectAll()
                .orderBy(StarWarsFilms.sequelId to SortOrder.ASC)
            query.forEach { println(it) }
        }
    }

    @Test
    fun groupBy() {
        exposed(StarWarsFilms) {
            // SELECT COUNT(STARWARSFILMS.SEQUEL_ID), STARWARSFILMS.DIRECTOR
            // FROM STARWARSFILMS GROUP BY STARWARSFILMS.DIRECTOR
            val query = StarWarsFilms
                .slice(StarWarsFilms.sequelId.count(), StarWarsFilms.director)
                .selectAll()
                .groupBy(StarWarsFilms.director)
            query.forEach { println(it) }
        }
    }

    @Test
    fun limit() {
        exposed(StarWarsFilms) {
            // SELECT STARWARSFILMS.ID, STARWARSFILMS.SEQUEL_ID, STARWARSFILMS."NAME", STARWARSFILMS.DIRECTOR
            // FROM STARWARSFILMS
            // WHERE STARWARSFILMS.SEQUEL_ID = 8 LIMIT 2 OFFSET 1
            val query = StarWarsFilms
                .select { StarWarsFilms.sequelId eq 8 }
                .limit(2, offset = 1)
            query.forEach { println(it) }
        }
    }

    @Test
    fun join() {
        exposed(StarWarsFilms, Players) {
            // TODO: 왜 에러가 나는 걸까? 매핑(reference)을 안해서?
            runCatching {
                val query = (Players innerJoin StarWarsFilms)
                    .slice(Players.name.count(), StarWarsFilms.name)
                    .select { StarWarsFilms.sequelId eq Players.sequelId }
                    .groupBy(StarWarsFilms.name)
                query.forEach { println(it) }
            }
        }

        // 직접 조인
        exposed(StarWarsFilms, Players) {
            val query = Players.join(
                StarWarsFilms,
                JoinType.INNER,
                additionalConstraint = { StarWarsFilms.sequelId eq Players.sequelId })
                .slice(Players.name.count(), StarWarsFilms.name)
                .selectAll()
                .groupBy(StarWarsFilms.name)
            query.forEach { println(it) }
        }
    }

    @Test
    fun union() {
        exposed(StarWarsFilms) {
            // SELECT STARWARSFILMS."NAME" FROM STARWARSFILMS WHERE STARWARSFILMS.DIRECTOR = 'George Lucas'
            // UNION
            // SELECT STARWARSFILMS."NAME" FROM STARWARSFILMS WHERE STARWARSFILMS.DIRECTOR = 'J.J. Abrams'
            val lucasDirectedQuery =
                StarWarsFilms.slice(StarWarsFilms.name).select { StarWarsFilms.director eq "George Lucas" }
            val abramsDirectedQuery =
                StarWarsFilms.slice(StarWarsFilms.name).select { StarWarsFilms.director eq "J.J. Abrams" }
            val filmNames = lucasDirectedQuery.union(abramsDirectedQuery).map { it[StarWarsFilms.name] }
            println("filmNames = $filmNames")
        }
    }

    @Test
    fun alias() {
        exposed(StarWarsFilms) {
            // SELECT ft1.ID, ft1.SEQUEL_ID, ft1."NAME", ft1.DIRECTOR FROM STARWARSFILMS ft1
            val filmTable1 = StarWarsFilms.alias("ft1")
            val query = filmTable1.selectAll() // can be used in joins etc
            query.forEach { println(it) }
        }

        exposed(StarWarsFilms) {
            // SELECT STARWARSFILMS."NAME", "sql"."NAME"
            // FROM STARWARSFILMS
            // INNER JOIN STARWARSFILMS "sql"
            // ON STARWARSFILMS.SEQUEL_ID = "sql".ID
            val sequelTable = StarWarsFilms.alias("sql")
            val originalAndSequelNames = StarWarsFilms
                .innerJoin(sequelTable, { sequelId }, { sequelTable[StarWarsFilms.id] })
                .slice(StarWarsFilms.name, sequelTable[StarWarsFilms.name])
                .selectAll()
                .map { it[StarWarsFilms.name] to it[sequelTable[StarWarsFilms.name]] }
            println("originalAndSequelNames = $originalAndSequelNames")
        }

        exposed(StarWarsFilms) {
            // 서브쿼리!
            // SELECT swf.ID, swf."NAME" FROM (SELECT STARWARSFILMS.ID, STARWARSFILMS."NAME" FROM STARWARSFILMS) swf
            val starWarsFilms = StarWarsFilms
                .slice(StarWarsFilms.id, StarWarsFilms.name)
                .selectAll()
                .alias("swf")
            val id = starWarsFilms[StarWarsFilms.id]
            val name = starWarsFilms[StarWarsFilms.name]
            starWarsFilms
                .slice(id, name)
                .selectAll()
                .map { it[id] to it[name] }
        }
    }
}
