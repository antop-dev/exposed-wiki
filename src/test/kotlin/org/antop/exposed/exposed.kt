package org.antop.exposed

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

fun exposed(
    vararg tables: Table,
    ddl: Boolean = true,
    init: (() -> Unit)? = null,
    logic: Transaction.() -> Unit
) {
    val uuid = UUID.randomUUID()
    Database.connect("jdbc:h2:mem:$uuid", driver = "org.h2.Driver")
    transaction {
        println("[begin] transaction")
        addLogger(StdOutSqlLogger)
        if (ddl) SchemaUtils.create(*tables)
        init?.let { it() }
        println("[begin] logic")
        logic()
        println("[end] logic")
    }
    println("[end] transaction")
}
