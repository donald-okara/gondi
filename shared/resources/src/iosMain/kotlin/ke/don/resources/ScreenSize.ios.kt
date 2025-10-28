package ke.don.resources

import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectGetHeight
import platform.CoreGraphics.CGRectGetWidth
import platform.UIKit.UIScreen

@OptIn(ExperimentalForeignApi::class)
actual fun getScreenWidth(): Float {
    val bounds = UIScreen.mainScreen.bounds
    return CGRectGetWidth(bounds).toFloat()
}

@OptIn(ExperimentalForeignApi::class)
actual fun getScreenHeight(): Float {
    val bounds = UIScreen.mainScreen.bounds
    return CGRectGetHeight(bounds).toFloat()
}
