package com.vungn.routes

import com.vungn.database.Database
import com.vungn.entities.UserEntity
import com.vungn.model.User
import io.ktor.http.*
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
        get {
            val users = Database.database.from(UserEntity).select().map(::resultRowToUser)
            call.respond(users)
        }
        get("{id?}") {
            val id = call.parameters.getOrFail("id").toInt()
            val user = Database.database.from(UserEntity).select().where { UserEntity.id eq id }.map(::resultRowToUser)
            call.respond(user)
        }
        post {
            val formParameters = call.receiveParameters()
            val name = formParameters.getOrFail("name")
            val email = formParameters.getOrFail("email")
            val avatar = formParameters.getOrFail("avatar")
            println("$name + $email + $avatar")
            val result = Database.database.insert(UserEntity) {
                set(it.name, name)
                set(it.email, email)
                set(it.avatar, avatar)
            }
            if (result == 1)
                call.respond(HttpStatusCode.OK)
            else
                call.respond(HttpStatusCode.BadRequest)
        }
    }
}