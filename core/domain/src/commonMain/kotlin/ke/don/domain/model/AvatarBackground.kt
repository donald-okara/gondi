package ke.don.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class AvatarBackground {
    // GREENS
    @SerialName("green_emerald") GREEN_EMERALD,   // #00E676
    @SerialName("green_minty") GREEN_MINTY,       // #1DE9B6
    @SerialName("green_neon") GREEN_NEON,         // #69F0AE
    @SerialName("green_leafy") GREEN_LEAFY,       // #00C853

    // YELLOWS
    @SerialName("yellow_bright") YELLOW_BRIGHT,   // #FFEA00
    @SerialName("yellow_sunny") YELLOW_SUNNY,     // #FFEB3B
    @SerialName("yellow_banana") YELLOW_BANANA,   // #FFF176
    @SerialName("yellow_golden") YELLOW_GOLDEN,   // #FFD600

    // PURPLES
    @SerialName("purple_electric") PURPLE_ELECTRIC, // #D500F9
    @SerialName("purple_orchid") PURPLE_ORCHID,     // #E040FB
    @SerialName("purple_lilac") PURPLE_LILAC,       // #B388FF
    @SerialName("purple_amethyst") PURPLE_AMETHYST, // #7C4DFF

    // EXTRAS
    @SerialName("cyan_bright") CYAN_BRIGHT,       // #18FFFF
    @SerialName("pink_hot") PINK_HOT,             // #FF4081
    @SerialName("orange_coral") ORANGE_CORAL      // #FF6E40
}