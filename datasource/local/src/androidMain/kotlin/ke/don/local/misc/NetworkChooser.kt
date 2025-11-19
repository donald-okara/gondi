package ke.don.local.misc

import android.content.Context
import android.content.Intent
import android.provider.Settings

class NetworkChooserAndroid(val context: Context): NetworkChooser{
    override fun open() {
        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
        context.startActivity(intent)
    }
}