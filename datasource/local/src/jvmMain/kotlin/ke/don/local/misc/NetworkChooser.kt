package ke.don.local.misc

class NetworkChooserJvm(): NetworkChooser{
    override fun open() {
        Runtime.getRuntime().exec("nm-connection-editor")
    }
}
