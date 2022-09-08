package com.vungn.database

import org.ktorm.database.Database

object Database {
    val database = Database.connect(
        url = "jdbc:mysql://localhost:3306/todo_app",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "root",
        password = ""
    )
}