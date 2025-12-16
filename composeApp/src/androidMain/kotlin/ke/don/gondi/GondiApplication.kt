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

import android.app.Application
import com.posthog.android.PostHogAndroid
import com.posthog.android.PostHogAndroidConfig
import io.kotzilla.sdk.analytics.koin.analytics
import ke.don.gondi.BuildConfig.POSTHOG_API_KEY
import ke.don.gondi.BuildConfig.POSTHOG_HOST
import org.koin.android.ext.koin.androidContext

class GondiApplication : Application() {
    override fun onCreate() {
        val config = PostHogAndroidConfig(
            apiKey = POSTHOG_API_KEY,
            host = POSTHOG_HOST,
        )

        PostHogAndroid.setup(this, config)

        super.onCreate()
        initKoin {
            androidContext(this@GondiApplication)
            analytics()
        }
    }
}
