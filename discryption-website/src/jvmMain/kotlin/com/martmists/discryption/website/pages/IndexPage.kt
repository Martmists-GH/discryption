package com.martmists.discryption.website.pages

import io.ktor.application.*
import kotlinx.html.*

class IndexPage(call: ApplicationCall) : Page(call) {
    override fun HEAD.render() {
        title("Home")
    }

    override fun DIV.render() {
        span {
            +"Hello, world!"
        }
    }
}
