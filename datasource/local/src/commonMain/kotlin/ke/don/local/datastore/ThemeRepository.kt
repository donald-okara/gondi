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

// commonMain
class ThemeRepository(private val themeStore: ThemeStore) {
    val theme = themeStore.themeFlow

    suspend fun setTheme(theme: Theme) = themeStore.setTheme(theme)
    suspend fun reset() = themeStore.clear()
}
