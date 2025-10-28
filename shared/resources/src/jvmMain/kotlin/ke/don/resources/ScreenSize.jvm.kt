package ke.don.resources

import java.awt.GraphicsEnvironment
import java.awt.Toolkit

actual fun getScreenWidth(): Float {
    if (GraphicsEnvironment.isHeadless()) return 0f
    val tk = Toolkit.getDefaultToolkit()
    val size = tk.screenSize
    val dpi = tk.screenResolution.takeIf { it > 0 } ?: 96
    // dp = px * 160 / dpi
    return size.width * 160f / dpi
}

actual fun getScreenHeight(): Float {
    if (GraphicsEnvironment.isHeadless()) return 0f
    val tk = Toolkit.getDefaultToolkit()
    val size = tk.screenSize
    val dpi = tk.screenResolution.takeIf { it > 0 } ?: 96
    return size.height * 160f / dpi
}
