package com.martmists.discryption.website.cards

import com.martmists.discryption.website.tables.CardCollectionTable
import com.martmists.discryption.website.tables.CardTable
import com.martmists.discryption.website.tables.DeckTable
import com.martmists.discryption.website.tables.PlayerTable
import com.martmists.discryption.website.transaction
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import kotlinx.serialization.Serializable

@Serializable
data class Card(
    val id: Int,
    val name: String,
    val isTerrain: Boolean,
    val temple: Temple,
    val rarity: Rarity,
    val attack: Int,
    val health: Int,
    val cost: Int,
    val costType: CostType,
    val sigils: List<Sigil>,
) {
    constructor(row: ResultRow) : this(
        row[CardTable.id].value,
        row[CardTable.name],
        row[CardTable.isTerrain],
        row[CardTable.temple],
        row[CardTable.rarity],
        row[CardTable.attack],
        row[CardTable.health],
        row[CardTable.cost],
        row[CardTable.costType],
        listOfNotNull(row[CardTable.sigil1], row[CardTable.sigil2]),
    )

    companion object {
        suspend fun collection(player: String) : List<Card> {
            return transaction {
                val cards = CardCollectionTable
                    .join(CardTable, JoinType.INNER, CardCollectionTable.card, CardTable.id)
                    .join(PlayerTable, JoinType.INNER, CardCollectionTable.owner, PlayerTable.id)
                    .select { PlayerTable.username eq player }
                val cardList = mutableListOf<Card>()
                cards.forEach {
                    val card = Card(it)
                    repeat(it[CardCollectionTable.quantity]) {
                        cardList.add(card)
                    }
                }
                cardList
            }
        }

        suspend fun deck(player: String) : List<Card> {
            return transaction {
                val cards = DeckTable
                    .join(CardTable, JoinType.INNER, DeckTable.card, CardTable.id)
                    .join(PlayerTable, JoinType.INNER, DeckTable.owner, PlayerTable.id)
                    .select { PlayerTable.username eq player }
                val cardList = mutableListOf<Card>()
                cards.forEach {
                    val card = Card(it)
                    repeat(it[DeckTable.quantity]) {
                        cardList.add(card)
                    }
                }
                cardList
            }
        }
    }
}
