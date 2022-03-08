package com.martmists.discryption.website.routes

import com.martmists.discryption.website.ext.respondPage
import com.martmists.discryption.website.pages.IndexPage
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Routing.frontend() {
    get("/") {
        call.respondPage(::IndexPage)
    }

    get("/lookup") {
        call.respond(HttpStatusCode.NotFound)
    }
}
