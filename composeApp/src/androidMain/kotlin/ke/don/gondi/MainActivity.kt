/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.gondi

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.jan.supabase.auth.handleDeeplinks
import ke.don.remote.api.SupabaseConfig.supabase
import ke.don.utils.Logger

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
        // Handle initial deeplink (cold start)
        intent?.let {
            debugDeeplink(it)
            supabase.handleDeeplinks(it)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent) // ✅ ensures Activity’s intent updates
        debugDeeplink(intent)
        supabase.handleDeeplinks(intent)
    }
}

fun debugDeeplink(intent: Intent) {
    val logger = Logger("DeepLink")
    val data = intent.data ?: return
    logger.info("🔗 Full deeplink URI = $data")
    logger.info("scheme=${data.scheme}, host=${data.host}, path=${data.path}, query=${data.query}, fragment=${data.fragment}")
}


