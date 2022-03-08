package com.martmists.discryption.website.tables

import org.jetbrains.exposed.sql.Table

object PackLootTable : Table() {
    val pack = integer("pack").references(PackTable.id)
    val card = integer("card").references(CardTable.id)
}
