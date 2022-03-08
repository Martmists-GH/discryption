package com.martmists.discryption.website.pages

import io.ktor.application.*
import kotlinx.html.*

abstract class Page(val call: ApplicationCall) {
    fun HTML.render() {
        head {
            meta(charset="utf-8") {}
            meta(content="IE=edge,chrome=1") {
                attributes["http-equiv"] = "X-UA-Compatible"
            }
            meta("viewport", "width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0") {}
            meta("viewport", "width=device-width") {}
            meta("theme-color", "#000000") {}
            meta("msapplication-navbutton-color", "#000000") {}
            meta("apple-mobile-web-app-status-bar-style", "#000000") {}
            link(rel="icon", href="/static/img/favicon.ico") {}
            link(rel="stylesheet", href="https://pro.fontawesome.com/releases/v6.0.0-beta1/css/all.css") {}
            link(rel="stylesheet", href="https://cdn.jsdelivr.net/npm/halfmoon@1.1.1/css/halfmoon-variables.min.css")
            link(rel="stylesheet", href="https://www.gethalfmoon.com/static/site/css/documentation-styles-4.css") {}
            script(type="text/javascript", src="https://cdn.jsdelivr.net/npm/halfmoon@1.1.1/js/halfmoon.min.js") {}
            script(type="text/javascript", src="https://cdnjs.cloudflare.com/ajax/libs/list.js/2.3.1/list.min.js") {}

            render()
        }
        body("with-custom-webkit-scrollbars with-custom-css-scrollbars dark-mode")  {
            attributes["data-set-preferred-mode-onload"] = "true"
            div("page-wrapper with-navbar with-sidebar with-transitions") {
                attributes["data-sidebar-type"] = "overlayed-sm-and-down"

                // TODO: Sidebar
                // TODO: Navbar

                div("content-wrapper") {
                    div("container-fluid") {
                        div("content") {
                            render()
                        }
                    }
                }
            }

            script(type="text/javascript", src = "/static/js/discryption-website.js") { }
        }
    }

    open fun HEAD.render() {

    }

    open fun DIV.render() {

    }
}
