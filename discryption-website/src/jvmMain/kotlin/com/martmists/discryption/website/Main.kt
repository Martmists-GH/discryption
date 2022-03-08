package com.martmists.discryption.website

import com.martmists.discryption.commons.readConfig
import com.martmists.discryption.website.auth.DiscordAuthSession
import com.martmists.discryption.website.config.WebsiteConfig
import com.martmists.discryption.website.routes.api.v1.api_v1
import com.martmists.discryption.website.routes.frontend
import com.martmists.discryption.website.routes.oauth
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.tomcat.*
import io.ktor.sessions.*

val config by lazy {
    readConfig<WebsiteConfig>("website.yaml")
}

val httpClient = HttpClient(CIO) {
    install(JsonFeature) {
        serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
            ignoreUnknownKeys = true
        })
    }
}

fun main() {
    initDB()

    embeddedServer(Tomcat, port = config.port, host = "127.0.0.1") {
        install(Compression)
        install(DefaultHeaders)
        install(PartialContent)
        install(AutoHeadResponse)
        install(CallLogging)
        install(XForwardedHeaderSupport)
        install(CachingHeaders)
        install(ContentNegotiation) {
            json()
        }
        install(Sessions) {
            cookie<DiscordAuthSession>("discord_account_cookie")
        }
        install(Authentication) {
            oauth("discord") {
                urlProvider = { config.auth.callbackUrl }
                providerLookup = {
                    OAuthServerSettings.OAuth2ServerSettings(
                        name = "discord",
                        authorizeUrl = "https://discord.com/api/oauth2/authorize",
                        accessTokenUrl = "https://discord.com/api/oauth2/token",
                        requestMethod = HttpMethod.Post,
                        clientId = config.auth.clientId,
                        clientSecret = config.auth.clientSecret,
                        defaultScopes = listOf("identify")
                    )
                }
                client = httpClient
            }
            session<DiscordAuthSession>("oauth") {
                validate { session ->
                    session
                }
                challenge {
                    call.respondRedirect("/login")
                }
            }
        }

        routing {
            frontend()
            api_v1()
            oauth()

            static("/static") {
                resources("static")
            }
        }
    }.start(wait = true)
}
