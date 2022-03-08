package com.martmists.discryption.website.routes

import com.martmists.discryption.website.auth.DiscordAuthSession
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun Routing.oauth() {
    authenticate("discord") {
        get("/login") {
            // Redirects to discord
        }

        get("/auth/callback") {
            val principal = call.principal<OAuthAccessTokenResponse.OAuth2>()
            val session = DiscordAuthSession(principal?.accessToken.toString())
            call.sessions.set(session)
            call.respondRedirect("/")
        }
    }
}
