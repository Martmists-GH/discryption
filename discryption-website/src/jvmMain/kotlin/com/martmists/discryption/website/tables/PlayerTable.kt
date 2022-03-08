package com.martmists.discryption.website.tables

import org.jetbrains.exposed.sql.Table

object PlayerTable : Table() {
    val id = integer("id")
    val username = varchar("username", 32)

    override val primaryKey = PrimaryKey(id)
}
