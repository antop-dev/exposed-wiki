package org.antop.exposed

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

fun exposed(
    vararg tables: Table, /* 생성할 테이블 */
    sqlFile: String? = null, /* 미리 실행할 SQL 파일 */
    logic: Transaction.() -> Unit /* 실행할 로직 */
) {
    val uuid = UUID.randomUUID()
    Database.connect("jdbc:h2:mem:$uuid", driver = "org.h2.Driver")
    transaction {
        println("[begin] transaction")
        addLogger(StdOutSqlLogger)
        if (tables.isNotEmpty()) {
            SchemaUtils.create(*tables)
        }
        sqlFile?.let {
            val stmt = this::class.java.classLoader.getResource(sqlFile)?.readText()
            stmt?.let { exec(it) }
        }
        commit()
        println("[begin] logic")
        logic()
        println("[end] logic")

    }
    println("[end] transaction")
}
