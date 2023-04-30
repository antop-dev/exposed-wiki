package org.antop.exposed.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object StarWarsFilms : IntIdTable() {
    val sequelId: Column<Int> = integer("sequel_id").uniqueIndex()
    val name: Column<String> = varchar("name", 30)
    val director: Column<String> = varchar("director", 50)

    // 어떤 데이터베이스 드라이버는 TEXT 타입을 즉시 로딩하지 않는다고 한다...
    // 이럴 때 즉시 로딩하고 싶으면 eagerLoading을 true로 설정하면 된다고 한다...
    val description = text("description", eagerLoading = true).nullable()
}
