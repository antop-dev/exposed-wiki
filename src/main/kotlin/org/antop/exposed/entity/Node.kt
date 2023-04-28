package org.antop.exposed.entity

import org.antop.exposed.table.NodeToNodes
import org.antop.exposed.table.Nodes
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Node(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Node>(Nodes)

    var name by Nodes.name
    var parents by Node.via(NodeToNodes.child, NodeToNodes.parent)
    var children by Node.via(NodeToNodes.parent, NodeToNodes.child)
}
