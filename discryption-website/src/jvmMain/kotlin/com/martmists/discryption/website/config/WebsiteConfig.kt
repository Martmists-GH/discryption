package com.martmists.discryption.website.config

data class WebsiteConfig(
    val port: Int,
    val auth: AuthConfig,
    val database: DatabaseConfig,
    val admins: List<String>,
)
