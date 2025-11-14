package ke.don.local

// jvmMain
import ke.don.domain.datastore.Theme
import ke.don.local.datastore.THEME_KEY
import ke.don.local.datastore.ThemeStore
import java.util.prefs.Preferences
import kotlinx.coroutines.flow.*

class JvmThemeStore : ThemeStore {
    private val prefs = Preferences.userRoot().node("app_prefs")
    private val key = THEME_KEY
    private val _themeFlow = MutableStateFlow(loadTheme())

    override val themeFlow: Flow<Theme?> = _themeFlow.asStateFlow()

    private fun loadTheme(): Theme? {
        val stored = prefs.get(key, null)
        return Theme.fromString(stored)
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
