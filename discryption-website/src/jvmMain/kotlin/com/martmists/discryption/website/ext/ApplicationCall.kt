package com.martmists.discryption.website.ext

import com.martmists.discryption.website.pages.Page
import io.ktor.application.*
import io.ktor.html.*

suspend fun ApplicationCall.respondPage(page: (call: ApplicationCall) -> Page) {
    respondHtml {
        page(this@respondPage).apply {
            render()
        }
    }
}
