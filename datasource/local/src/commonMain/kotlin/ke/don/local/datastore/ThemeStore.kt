package ke.don.local.datastore

import androidx.datastore.preferences.core.stringPreferencesKey
import ke.don.domain.datastore.Theme
import kotlinx.coroutines.flow.Flow

interface ThemeStore {
    val themeFlow: Flow<Theme?>
    suspend fun setTheme(theme: Theme)
    suspend fun clear()
}

const val THEME_KEY = "theme"
