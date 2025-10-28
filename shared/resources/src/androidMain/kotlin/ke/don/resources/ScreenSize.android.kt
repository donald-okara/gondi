package ke.don.resources

import android.content.res.Configuration
import android.content.res.Resources

actual fun getScreenWidth(): Float {
    val configuration = android.content.res.Resources.getSystem().configuration
    val sw = configuration.screenWidthDp
    // dp; fallback if undefined
    return if (sw != Configuration.SCREEN_WIDTH_DP_UNDEFINED) {
        sw.toFloat()
    } else {
        android.content.res.Resources.getSystem().displayMetrics.run { widthPixels / density }
    }
}

actual fun getScreenHeight(): Float {
    val configuration = android.content.res.Resources.getSystem().configuration
    val sh = configuration.screenHeightDp
    return if (sh != Configuration.SCREEN_HEIGHT_DP_UNDEFINED) {
        sh.toFloat()
    } else {
        Resources.getSystem().displayMetrics.run { heightPixels / density }
    }
}
