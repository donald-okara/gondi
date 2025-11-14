package ke.don.local.datastore

import ke.don.domain.datastore.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.prefs.Preferences

class JvmThemeStore : ThemeStore {
    private val prefs = Preferences.userRoot().node("app_prefs")
    private val key = THEME_KEY
    private val _themeFlow = MutableStateFlow(loadTheme())

    override val themeFlow: Flow<Theme?> = _themeFlow.asStateFlow()

    private fun loadTheme(): Theme? {
        val stored = prefs.get(key, null)
        return Theme.Companion.fromString(stored)
    }

    override suspend fun setTheme(theme: Theme) {
        prefs.put(key, theme.name)
        _themeFlow.value = theme
    }

    override suspend fun clear() {
        prefs.remove(key)
        _themeFlow.value = Theme.System
    }
}