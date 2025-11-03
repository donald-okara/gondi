package ke.don.local.db

import app.cash.sqldelight.db.SqlDriver

interface DatabaseFactory {
    fun createDriver(): SqlDriver
}