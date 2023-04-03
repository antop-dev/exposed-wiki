package org.antop.exposed.table

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test

class CitiesTest {

    @Test
    fun insert() {
        //an example connection to H2 DB
        Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver")

        transaction {
            // print sql to std-out
            addLogger(StdOutSqlLogger)

            SchemaUtils.create(Cities)

            // insert new city. SQL: INSERT INTO Cities (name) VALUES ('St. Petersburg')
            val stPeteId = Cities.insert {
                it[name] = "St. Petersburg"
            } get Cities.id

            println("inserted id = $stPeteId")

            // 'select *' SQL: SELECT Cities.id, Cities.name FROM Cities
            Cities.selectAll().forEach { println(it) }
        }
    }
}
