package org.antop.exposed.table

import org.jetbrains.exposed.dao.id.IntIdTable

object Actors : IntIdTable() {
    val firstname = varchar("firstname", 50)
    val lastname = varchar("lastname", 50)
}
