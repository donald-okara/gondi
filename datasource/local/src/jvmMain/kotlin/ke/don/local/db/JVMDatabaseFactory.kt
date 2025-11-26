/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.local.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.io.File

class JVMDatabaseFactory : DatabaseFactory {

    private val dbFileName = "gondi.db"

    /**
     * Creates a fresh database by deleting any existing file.
     * Use this temporarily for testing or resetting your DB.
     */
    override fun createDriver(): SqlDriver {
        val dbFile = File(dbFileName)

        // Delete existing DB if it exists
        if (dbFile.exists()) {
            dbFile.delete()
        }

        val driver = JdbcSqliteDriver("jdbc:sqlite:$dbFileName")
        GondiDatabase.Schema.create(driver) // Create schema from scratch
        return driver
    }

    /**
     * Normal driver: creates database only if it doesn't exist.
     */
//    override fun createDriver(): SqlDriver {
//        val dbFile = File(dbFileName)
//        val driver = JdbcSqliteDriver("jdbc:sqlite:$dbFileName")
//        if (!dbFile.exists()) {
//            GondiDatabase.Schema.create(driver)
//        }
//        return driver
//    }

    /**
     * In-memory driver for testing purposes.
     */
    fun createInMemoryDriver(): SqlDriver {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        GondiDatabase.Schema.create(driver)
        return driver
    }
}
