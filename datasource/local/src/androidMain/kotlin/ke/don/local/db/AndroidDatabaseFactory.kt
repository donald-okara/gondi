package ke.don.local.db

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

class AndroidDatabaseFactory(
    private val context: Context
): DatabaseFactory {
    override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            GondiDatabase.Schema,
            context,
            "gondi.db"
        )
    }
}