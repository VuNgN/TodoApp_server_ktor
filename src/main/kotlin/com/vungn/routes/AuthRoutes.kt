package com.vungn.routes

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.vungn.database.Database
import com.vungn.entities.UserEntity
import com.vungn.model.User
import com.vungn.model.UserRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.ktorm.dsl.*
import java.util.*

private fun resultRowToUser(row: QueryRowSet) = User(
    id = row[UserEntity.id] ?: -1,
    name = row[UserEntity.name] ?: "",
    email = row[UserEntity.email] ?: "",
    avatar = row[UserEntity.avatar] ?: ""
)

fun Route.authRoutes() {
    route("auth") {
        post {
            val formParameters = call.receiveParameters()
            val token = formParameters.getOrFail("token")
            val verifier =
                GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory())
                    .setAudience(Collections.singletonList("481520261748-91dhbr38ucoc0phjhbfhp3drhl29rlff.apps.googleusercontent.com"))
                    .build()
            val idToken = verifier.verify(token)
            if (idToken != null) {
                val payload = idToken.payload

                val userId = payload.subject
                println("UserId: $userId")
                val email = payload.email
                val emailVerified = payload.emailVerified as Boolean
                val name = payload["name"]
                val pictureUrl = payload["picture"]
                val locale = payload["locale"]
                val familyName = payload["family_name"]
                val givenName = payload["given_name"]

                val isAlreadyExistsUser =
                    Database.database.from(UserEntity).select().where { UserEntity.email eq email }
                        .map(::resultRowToUser).isNotEmpty()

                if (isAlreadyExistsUser) {
                    call.respond(UserRequest(name = name.toString(), email = email, avatar = pictureUrl.toString()))
                } else {
                    val result = Database.database.insert(UserEntity) {
                        set(it.name, name.toString())
                        set(it.email, email.toString())
                        set(it.avatar, pictureUrl.toString())
                    }
                    if (result == 1)
                        call.respond(UserRequest(name = name.toString(), email = email, avatar = pictureUrl.toString()))
                    else
                        call.respond(HttpStatusCode.BadRequest)
                }

                println(
                    "Email: $email \n  " +
                            "Email verified: $emailVerified \n " +
                            "Name: $name \n " +
                            "Picture url: $pictureUrl \n " +
                            "Locale: $locale \n " +
                            "Family name: $familyName \n " +
                            "Given name: $givenName"
                )
            } else {
                println("id token null")
            }
        }
    }
}