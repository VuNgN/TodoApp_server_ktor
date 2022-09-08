package com.vungn.model

import kotlinx.serialization.Serializable

@Serializable
data class User(val id: Int, val name: String, val email: String, val avatar: String)

@Serializable
data class UserRequest(val name: String, val email: String, val avatar: String)