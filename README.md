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
