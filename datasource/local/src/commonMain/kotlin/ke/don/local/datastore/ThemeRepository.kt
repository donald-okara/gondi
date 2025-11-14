package ke.don.local.datastore

import ke.don.domain.datastore.Theme

// commonMain
class ThemeRepository(private val themeStore: ThemeStore) {
    val theme = themeStore.themeFlow

    suspend fun setTheme(theme: Theme) = themeStore.setTheme(theme)
    suspend fun reset() = themeStore.clear()
}
