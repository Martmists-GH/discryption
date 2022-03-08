package com.martmists.discryption.website.cards

import com.martmists.discryption.website.tables.CardTable
import com.martmists.discryption.website.tables.PackLootTable
import com.martmists.discryption.website.tables.PackTable
import com.martmists.discryption.website.transaction
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select

@Serializable
data class Pack(
    private val id: Int,
    val name: String,
) {
    constructor(row: ResultRow) : this(
            row[PackTable.id].value,
            row[PackTable.name]
    )

    suspend fun drawInscryption(n: Int) : List<Card> {
        return transaction {
            val allMatching = PackLootTable
                .join(CardTable, JoinType.LEFT, PackLootTable.card, CardTable.id)
                .select { PackLootTable.pack eq id }.toList()
            val rareMatching = allMatching.filter { it[CardTable.rarity] == Rarity.Rare }
            val commonMatching = allMatching.filter { it[CardTable.rarity] == Rarity.Common }
            val commonNotMatching = PackLootTable
                .join(CardTable, JoinType.LEFT, PackLootTable.card, CardTable.id)
                .select { PackLootTable.pack neq id and (CardTable.rarity eq Rarity.Common) }.toList()

            (0 until n).map { i ->
                when {
                    i >= 3 -> {
                        commonMatching.random()
                    }
                    i != 1 -> {
                        commonNotMatching.random()
                    }
                    else -> {
                        rareMatching.random()
                    }
                }.let(::Card)
            }
        }
    }

    suspend fun draw(n: Int) : List<Card> {
        return transaction {
            val cards = PackLootTable
                .join(CardTable, JoinType.LEFT, PackLootTable.card, CardTable.id)
                .select { PackLootTable.pack eq id }.toList()

            val pool = mutableListOf<Card>()

            cards.forEach {
                val card = Card(it)
                if (card.rarity == Rarity.Common) {
                    repeat(10) {
                        pool.add(card)
                    }
                } else {
                    pool.add(card)
                }
            }

            (0 until n).map {
                val c = pool.random()
                pool.remove(c)
                c
            }
        }
    }
}
