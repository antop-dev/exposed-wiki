package org.antop.exposed.table

import org.jetbrains.exposed.dao.id.IntIdTable

object Cities : IntIdTable() {
    val name = varchar("name", 50)
}
