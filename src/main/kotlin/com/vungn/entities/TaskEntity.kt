package com.vungn.entities

import org.ktorm.schema.*

object TaskEntity : Table<Nothing>("task") {
    val id = int("_id").primaryKey()
    val userId = int("userId")
    val name = varchar("name")
    val description = text("description")
    val repeation = int("repeation")
    val createOn = timestamp("createOn")
    val due = timestamp("due")
    val state = int("state")
}