package ke.don.local.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver

class JVMDatabaseFactory: DatabaseFactory {
    override fun createDriver(): SqlDriver {
        val driver = JdbcSqliteDriver("jdbc:sqlite:gondi.db")

        // Create schema only if it doesn't exist yet
        GondiDatabase.Schema.create(driver)

        return driver
    }
}