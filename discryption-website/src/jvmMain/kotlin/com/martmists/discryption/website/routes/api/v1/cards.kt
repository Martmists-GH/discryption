package com.martmists.discryption.website.routes.api.v1

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
        val config = call.receive<CardRenderPayload>()
        val card = transaction {
            val id = call.parameters["id"]!!.toInt()
            val row = CardTable.select { CardTable.id eq id }.firstOrNull()
                ?: let {
                    call.respond(HttpStatusCode.NotFound, emptyMap<String, String>())
                    null
                }
            row?.let(::Card)
        } ?: return@get
        val image = httpClient.get<ByteArray>("http://localhost:5000/card/${card.name}") {
            url.parameters.also {
                it["rare"] = if (card.rarity == Rarity.Rare) "true" else "false"
                it["terrain"] = if (card.isTerrain) "true" else "false"
                it["conduit"] = if (config.inConduit) "true" else "false"
                it["temple"] = card.temple.name
                it["opponent"] = if (config.opponent) "true" else "false"
                it["health"] = config.health.toString()
                it["max_health"] = card.health.toString()
                it["attack"] = config.attack.toString()
                it["max_attack"] = card.attack.toString()
                it["cost"] = card.cost.toString()
                it["cost_type"] = card.costType.ordinal.toString()
                it["sigils"] = card.sigils.joinToString(",") { it.name }
            }
        }
        call.respond(image)
    }
}
