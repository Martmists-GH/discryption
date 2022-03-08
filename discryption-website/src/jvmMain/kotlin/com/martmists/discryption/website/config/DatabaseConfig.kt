package com.martmists.discryption.website.config

data class DatabaseConfig(
    val driver: String,
    val host: String,
    val port: Int,
    val username: String,
    val password: String,
    val database: String
)
