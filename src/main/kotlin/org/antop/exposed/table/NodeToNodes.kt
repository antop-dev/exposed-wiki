package org.antop.exposed.table

import org.jetbrains.exposed.sql.Table

object NodeToNodes : Table() {
    val parent = reference("parent_node_id", Nodes)
    val child = reference("child_user_id", Nodes)
}
