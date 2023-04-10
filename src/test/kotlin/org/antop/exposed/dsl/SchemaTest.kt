package org.antop.exposed.dsl

import org.antop.exposed.exposed
import org.jetbrains.exposed.sql.Schema
import org.jetbrains.exposed.sql.SchemaUtils
import org.junit.jupiter.api.Test

class SchemaTest {
    @Test
    fun schema() {
        exposed {
            val schema = Schema("my_schema") // my_schema is the schema name.
            // CREATE SCHEMA IF NOT EXISTS my_schema
            SchemaUtils.createSchema(schema)
            // DROP SCHEMA IF EXISTS my_schema
            SchemaUtils.dropSchema(schema)
        }
    }

    @Test
    fun owner() {
        exposed {
            exec("create role owner")
            val schema = Schema("my_schema", authorization = "owner")
            // REATE SCHEMA IF NOT EXISTS my_schema AUTHORIZATION  owner
            SchemaUtils.createSchema(schema)
        }
    }
}
