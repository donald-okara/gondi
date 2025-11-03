package ke.don.local.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

class IOSDatabaseFactory: DatabaseFactory {
    override fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            GondiDatabase.Schema,
            "gondi.db"
        )
    }
}