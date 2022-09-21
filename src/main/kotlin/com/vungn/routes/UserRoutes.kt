package com.vungn.routes

import com.vungn.database.Database
import com.vungn.entities.UserEntity
import com.vungn.model.User
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.ktorm.dsl.*

private fun resultRowToUser(row: QueryRowSet) = User(
    id = row[UserEntity.id] ?: -1,
    name = row[UserEntity.name] ?: "",
    email = row[UserEntity.email] ?: "",
    avatar = row[UserEntity.avatar] ?: ""
)

fun Route.userRoutes() {
    route("user") {
        get("{id?}") {
            val id = call.parameters.getOrFail("id").toInt()
            val user = Database.database.from(UserEntity).select().where { UserEntity.id eq id }.map(::resultRowToUser)
            call.respond(user)
        }
        get("search") {
            val formParameters = call.receiveParameters()
            val keyword = formParameters.getOrFail("keyword")
            val users = Database.database.from(UserEntity).select()
                .where { (UserEntity.name like "%$keyword%") or (UserEntity.email like "%$keyword%") }
                .map(::resultRowToUser)
            call.respond(users)
        }
    }
}