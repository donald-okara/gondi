package ke.don.local.misc

import platform.Foundation.NSURL
import platform.UIKit.UIApplication


class NetworkChooserIos(): NetworkChooser{
    override fun open() {
        val url = NSURL(string = "App-Prefs:root=WIFI")
        UIApplication.sharedApplication.openURL(url)
    }
}