package com.martmists.discryption.website.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object PackTable : IntIdTable() {
    val name = varchar("name", 255)
}
