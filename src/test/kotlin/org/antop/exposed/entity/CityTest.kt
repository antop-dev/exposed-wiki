package org.antop.exposed.entity

import org.antop.exposed.table.Cities
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test

class CityTest {

    @Test
    fun save() {
        //an example connection to H2 DB
        Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver")

        transaction {
            // print sql to std-out
            addLogger(StdOutSqlLogger)

            SchemaUtils.create(Cities)

            // insert new city. SQL: INSERT INTO Cities (name) VALUES ('St. Petersburg')
            val stPete = City.new {
                name = "St. Petersburg"
            }
            println("saved = $stPete")

            City.new {
                name = "Seoul"
            }

            // 'select *' SQL: SELECT Cities.id, Cities.name FROM Cities
            City.all()
                .forEach { println("city = $it") }
        }
    }
}
