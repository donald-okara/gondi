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

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import ke.don.domain.datastore.Theme
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "app_prefs")

class AndroidThemeStore(private val context: Context) : ThemeStore {
    private val ds = context.dataStore
    val themeKey = stringPreferencesKey(THEME_KEY)

    override val themeFlow = ds.data
        .map { prefs -> Theme.fromString(prefs[themeKey]) }

    override suspend fun setTheme(theme: Theme) {
        ds.edit { prefs -> prefs[themeKey] = theme.name }
    }

    override suspend fun clear() {
        ds.edit { it.clear() }
    }
}
