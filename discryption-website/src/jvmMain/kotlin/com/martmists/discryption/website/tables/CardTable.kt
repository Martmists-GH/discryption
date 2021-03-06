package com.martmists.discryption.website.tables

import com.martmists.discryption.website.cards.*
import org.jetbrains.exposed.dao.id.IntIdTable

object CardTable : IntIdTable() {
    val name = varchar("name", 255)
    val internalName = varchar("internal_name", 255)
    val description = varchar("description", 255).nullable()
    val isTerrain = bool("is_terrain")
    val temple = enumeration("temple", Temple::class)
    val rarity = enumeration("rarity", Rarity::class)
    val attack = integer("attack").check { it greaterEq 0 }
    val health = integer("health").check { it greaterEq 1 }
    val cost = integer("cost").check { it greaterEq 0 }
    val costType = enumeration("cost_type", CostType::class)
    val sigil1 = enumeration("sigil1", Sigil::class).nullable()
    val sigil2 = enumeration("sigil2", Sigil::class).nullable()
    val releaseSet = enumeration("release_set", ReleaseSet::class)
}
