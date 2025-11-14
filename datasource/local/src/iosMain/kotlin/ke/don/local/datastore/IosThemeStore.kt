/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.local.datastore

import ke.don.domain.datastore.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import platform.Foundation.NSUserDefaults

class IosThemeStore : ThemeStore {
    private val defaults = NSUserDefaults.standardUserDefaults()
    private val themeKey = THEME_KEY
    private val _themeFlow = MutableStateFlow(loadTheme())

    override val themeFlow: Flow<Theme?> = _themeFlow.asStateFlow()

    private fun loadTheme(): Theme? {
        val stored = defaults.stringForKey(themeKey)
        return stored?.let { Theme.fromString(it) }
    }

    override suspend fun setTheme(theme: Theme) {
        defaults.setObject(theme.name, forKey = themeKey)
        _themeFlow.value = theme
    }

    override suspend fun clear() {
        defaults.removeObjectForKey(themeKey)
        _themeFlow.value = Theme.System
    }
}
