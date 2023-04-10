package org.antop.exposed.dsl

import org.antop.exposed.exposed
import org.antop.exposed.table.Cities
import org.antop.exposed.table.Users
import org.jetbrains.exposed.sql.*
import org.junit.jupiter.api.Test

class InsertSelect {
    @Test
    fun insertSelect() {
        exposed(Users, Cities) {
            // INSERT INTO CITIES ("NAME") SELECT SUBSTRING(USERS."NAME", 1, 2) FROM USERS ORDER BY USERS.ID ASC LIMIT 2
            val substring = Users.name.substring(1, 2)
            Cities.insert(Users.slice(substring).selectAll().orderBy(Users.id).limit(2))
        }

        exposed(Users) {
            val userCount = Users.selectAll().count()
            // INSERT INTO USERS ("NAME", ID) SELECT 'Foo', SUBSTRING(CAST(RANDOM() AS VARCHAR(255)), 1, 10) FROM USER
            Users.insert(
                Users.slice(
                    stringParam("Foo"),
                    Random().castTo<String>(VarCharColumnType()).substring(1, 10)
                ).selectAll(),
                columns = listOf(Users.name, Users.id)
            )
        }
    }
}
