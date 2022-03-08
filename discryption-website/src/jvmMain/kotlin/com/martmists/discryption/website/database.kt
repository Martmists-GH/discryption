package com.martmists.discryption.website

import com.martmists.discryption.website.tables.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync
import java.sql.Connection

private val db by lazy {
    val cfg = config.database
    when (cfg.driver) {
        "sqlite" -> {
            Database.connect("jdbc:sqlite:${cfg.database}.sqlite", driver = "org.sqlite.JDBC").also {
                TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
            }
        }
        "postgres" -> {
            Database.connect(
                "jdbc:postgresql://${cfg.host}:${cfg.port}/${cfg.database}",
                driver = "org.postgresql.Driver",
                user = cfg.username,
                password = cfg.password
            )
        }
        else -> throw IllegalArgumentException("Unknown database driver: ${cfg.driver}")
    }
}

fun initDB() {
    org.jetbrains.exposed.sql.transactions.transaction(db) {
        SchemaUtils.createMissingTablesAndColumns(
            PlayerTable,
            CardTable,
            PackTable,
            CardCollectionTable,
            PackLootTable,
        )
    }
}

suspend fun <T> transaction(block: suspend () -> T) : T {
    return suspendedTransactionAsync(Dispatchers.IO, db) {
        block()
    }.await()
}
