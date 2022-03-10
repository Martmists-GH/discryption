package com.martmists.discryption.website.routes.api.v1

import com.martmists.discryption.renderer.Renderer
import com.martmists.discryption.renderer.RenderInfo
import com.martmists.discryption.website.cards.Card
import com.martmists.discryption.website.cards.Rarity
import com.martmists.discryption.website.httpClient
import com.martmists.discryption.website.payloads.CardRenderPayload
import com.martmists.discryption.website.tables.CardTable
import com.martmists.discryption.website.transaction
import io.ktor.application.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.jetbrains.exposed.sql.select

fun Routing.cards() {
    // Lookup
    get("/api/v1/cards/lookup/{query}") {
        transaction {
            val query = call.parameters["query"]!!
            val cards = CardTable.select { CardTable.name like "%$query%" }.map {
                Card(it)
            }
            call.respond(cards)
        }
    }

    // By ID
    get("/api/v1/cards/{id}") {
        transaction {
            val id = call.parameters["id"]!!.toInt()
            val card = CardTable.select { CardTable.id eq id }.firstOrNull()
                ?: return@transaction call.respond(HttpStatusCode.NotFound, emptyMap<String, String>())

            call.respond(Card(card))
        }
    }

    // Render
    get("/api/v1/cards/{id}/render") {
        val id = call.parameters["id"]!!.toInt()
        val params = call.request.queryParameters
        val card = transaction {
            val row = CardTable.select { CardTable.id eq id }.firstOrNull()
            row?.let(::Card)
        } ?: return@get call.respond(HttpStatusCode.NotFound)
        val info = try {
            RenderInfo(
                card.internalName,
                card.rarity == Rarity.Rare,
                card.isTerrain,
                params["conduit"]?.toBoolean() ?: false,
                card.temple.name.lowercase(),
                params["opponent"]?.toBoolean() ?: false,
                params["health"]?.toInt() ?: card.health,
                params["attack"]?.toInt() ?: card.attack,
                card.health,
                card.attack,
                card.cost,
                card.costType.ordinal,
                card.sigils.map { it.name.lowercase() }
            )
        } catch(e: Exception) {
            return@get call.respond(HttpStatusCode.BadRequest)
        }
        val image = Renderer.render(info)
        call.respondBytes(image, ContentType.Image.PNG)
    }
}
