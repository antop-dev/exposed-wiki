package org.antop.exposed.dsl

import org.antop.exposed.exposed
import org.antop.exposed.table.Cities
import org.jetbrains.exposed.sql.batchInsert
import org.junit.jupiter.api.Test

class BatchInsertTest {
    @Test
    fun batchInsert() {
        exposed(Cities) {
            val cityNames = listOf("Paris", "Moscow", "Helsinki")
            // 데이터베이스에 따라 벌크 인서트가 안될수도 될 수도 있다.
            // INSERT INTO CITIES ("NAME") VALUES ('Paris')
            // INSERT INTO CITIES ("NAME") VALUES ('Moscow')
            // INSERT INTO CITIES ("NAME") VALUES ('Helsinki')
            val allCitiesID = Cities.batchInsert(cityNames) { name ->
                this[Cities.name] = name
            }

            for (id in allCitiesID) {
                println(id)
            }
        }
    }
}
