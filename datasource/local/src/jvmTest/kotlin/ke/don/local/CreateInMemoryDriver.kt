/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import ke.don.local.db.DatabaseFactory
import ke.don.local.db.GondiDatabase
import ke.don.local.db.JVMDatabaseFactory
import ke.don.local.db.LocalDatabase

fun createDb(): LocalDatabase {
    val driver = JVMDatabaseFactory().createInMemoryDriver() // your helper for in-memory SQLDelight
    return LocalDatabase(object : DatabaseFactory {
        override fun createDriver() = driver
    })
}

