package com.vungn.model

import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class Task(
    val id: Int,
    val userId: Int,
    val name: String,
    val description: String,
    val repeation: Int,
    @Serializable(with = InstantSerializer::class) val createOn: Instant,
    @Serializable(with = InstantSerializer::class) val due: Instant,
    val state: Int
)

@Serializable
data class TaskRequest(
    val userId: Int,
    val name: String,
    val description: String,
    val repeation: Int,
    @Serializable(with = InstantSerializer::class) val createOn: Instant,
    @Serializable(with = InstantSerializer::class) val due: Instant,
    val state: Int
)
