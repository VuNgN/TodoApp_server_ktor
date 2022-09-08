package com.vungn.entities

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.text
import org.ktorm.schema.varchar

object UserEntity : Table<Nothing>("users") {
    val id = int("_id").primaryKey()
    val name = varchar("name")
    val email = varchar("email")
    val avatar = text("avatar")
}