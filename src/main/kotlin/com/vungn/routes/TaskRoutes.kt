package com.vungn.routes

import com.vungn.database.Database
import com.vungn.entities.TaskEntity
import com.vungn.model.Task
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.ktorm.dsl.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private fun resultRowToTask(row: QueryRowSet) = Task(
    id = row[TaskEntity.id]!!,
    userId = row[TaskEntity.userId]!!,
    name = row[TaskEntity.name]!!,
    description = row[TaskEntity.description]!!,
    repeation = row[TaskEntity.repeation]!!,
    createOn = row[TaskEntity.createOn]!!,
    due = row[TaskEntity.due]!!,
    state = row[TaskEntity.state]!!
)

private fun String.toInstant(): Instant {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val temporalAccessor = formatter.parse(this)
    val localDateTime = LocalDateTime.from(temporalAccessor)
    val zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault())
    return Instant.from(zonedDateTime)
}

fun Route.taskRoutes() {
    route("task") {
        get {
            val tasks = Database.database.from(TaskEntity).select().map(::resultRowToTask)
            call.respond(tasks)
        }
        get("{id?}") {
            val id = call.parameters.getOrFail("id").toInt()
            val task = Database.database.from(TaskEntity).select().where(TaskEntity.id eq id).map(::resultRowToTask)
            call.respond(task)
        }
        post {
            val formParameters = call.receiveParameters()
            val userId = formParameters.getOrFail("userId").toInt()
            val name = formParameters.getOrFail("name")
            val desc = formParameters.getOrFail("description")
            val repeat = formParameters.getOrFail("repeation").toInt()
            val createOn = formParameters.getOrFail("createOn").toInstant()
            val due = formParameters.getOrFail("due").toInstant()
            val state = formParameters.getOrFail("state").toInt()
            val result = Database.database.insert(TaskEntity) {
                set(it.userId, userId)
                set(it.name, name)
                set(it.description, desc)
                set(it.repeation, repeat)
                set(it.createOn, createOn)
                set(it.due, due)
                set(it.state, state)
            }
            if (result == 1) call.respond(HttpStatusCode.OK)
            else call.respond(HttpStatusCode.BadRequest)
        }
        patch("{id?}") {
            val id = call.parameters.getOrFail("id").toInt()
            val formParameters = call.receiveParameters()
            val state = formParameters.getOrFail("state").toInt()
            val result = Database.database.update(TaskEntity) {
                set(it.state, state)
                where {
                    it.id eq id
                }
            }
            if (result == 1) call.respond(HttpStatusCode.OK)
            else call.respond(HttpStatusCode.BadRequest)
        }
    }
}