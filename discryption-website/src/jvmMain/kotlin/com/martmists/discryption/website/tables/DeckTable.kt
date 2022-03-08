package com.martmists.discryption.website.tables

import org.jetbrains.exposed.sql.Table

object DeckTable : Table() {
    val owner = integer("owner").references(PlayerTable.id)
    val card = integer("card").references(CardTable.id)
    val quantity = integer("quantity").check { it greaterEq 1 }

    override val primaryKey = PrimaryKey(owner, card)
}
