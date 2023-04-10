package org.antop.exposed.table

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object Players : Table() {
    val sequelId: Column<Int> = integer("sequel_id").uniqueIndex()
    val name: Column<String> = varchar("name", 50)
}
