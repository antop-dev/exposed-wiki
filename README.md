# exposed-wiki

https://github.com/JetBrains/Exposed/wiki

* [Getting Started](https://github.com/JetBrains/Exposed/wiki/Getting-Started)
* [DSL API](https://github.com/JetBrains/Exposed/wiki/DSL)
* [DAO API](https://github.com/JetBrains/Exposed/wiki/DAO)

보일러 플레이트 코드를 최소화하기 위해 헬퍼 함수를 하나 만듬

```kotlin
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
```

사용법

```kotlin
class StarWarsFilmsTest {
    @Test
    fun create() {
        exposed(StarWarsFilms, Users, Cities) {
            // do something
        }
    }
}
```
