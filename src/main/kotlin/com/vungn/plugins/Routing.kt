package com.vungn.plugins

import com.vungn.routes.taskRoutes
import com.vungn.routes.userRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        userRoutes()
        taskRoutes()
    }
}
