package org.antop.exposed.dsl

import org.antop.exposed.exposed
import org.antop.exposed.table.StarWarsFilms
import org.jetbrains.exposed.sql.*
import org.junit.jupiter.api.Test

class SequenceTest {
    @Test
    fun define() {
        exposed(StarWarsFilms) {
            val seq = Sequence("my_sequence")
            // CREATE SEQUENCE IF NOT EXISTS my_sequenc
            SchemaUtils.createSequence(seq)

            val nextVal = seq.nextIntVal()
            // INSERT INTO STARWARSFILMS (DIRECTOR, ID, "NAME", SEQUEL_ID) VALUES ('Rian Johnson', NEXT VALUE FOR my_sequence, 'The Last Jedi', 8)
            val id = StarWarsFilms.insertAndGetId {
                it[id] = nextVal
                it[name] = "The Last Jedi"
                it[sequelId] = 8
                it[director] = "Rian Johnson"
            }
            println("saved id = $id")

            // SELECT NEXT VALUE FOR my_sequence FROM STARWARSFILMS
            val firstValue = StarWarsFilms.slice(nextVal).selectAll().single()[nextVal]
            println("firstValue = $firstValue")

            // DROP SEQUENCE IF EXISTS my_sequence
            SchemaUtils.dropSequence(seq)
        }
    }
}
