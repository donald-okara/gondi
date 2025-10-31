package ke.don.gondi

import android.app.Application
import org.koin.android.ext.koin.androidContext

class GondiApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin{
            androidContext(this@GondiApplication)
        }
    }
}