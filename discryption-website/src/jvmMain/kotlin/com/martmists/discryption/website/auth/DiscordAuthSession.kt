package com.martmists.discryption.website.auth

import com.martmists.discryption.commons.LazyThreadedValue
import com.martmists.discryption.website.config
import com.martmists.discryption.website.httpClient
import io.ktor.auth.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

data class DiscordAuthSession(
    val accessToken: String,
) : Principal {
    @Serializable
    data class DiscordUser(
        val id: String,
        val username: String,
        val discriminator: String,
    ) {
        val isAdmin by lazy {
            id in config.admins
        }
    }

    val userData by LazyThreadedValue {
        runBlocking {
            httpClient.get<DiscordUser>("https://discordapp.com/api/users/@me") {
                header(HttpHeaders.Authorization, "Bearer $accessToken")
            }
        }
    }
}
