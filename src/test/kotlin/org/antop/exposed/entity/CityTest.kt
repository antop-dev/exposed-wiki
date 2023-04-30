package org.antop.exposed.entity

import org.antop.exposed.exposed
import org.antop.exposed.table.Cities
import org.junit.jupiter.api.Test

class CityTest {

    @Test
    fun save() {
        exposed(Cities) {
            // insert new city. SQL: INSERT INTO Cities (name) VALUES ('St. Petersburg')
            val stPete = City.new {
                name = "St. Petersburg"
            }
            println("saved = $stPete")

            City.new {
                name = "Seoul"
            }

            // 'select *' SQL: SELECT Cities.id, Cities.name FROM Cities
            City.all().forEach { println("city = $it") }
        }
    }
}
