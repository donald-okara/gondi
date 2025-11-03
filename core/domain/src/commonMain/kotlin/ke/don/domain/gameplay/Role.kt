package ke.don.domain.gameplay

enum class Role(
    val faction: Faction,
    val canActInSleep: Boolean = false,
    val canAccuse: Boolean = true,
    val canVote: Boolean = true
) {
    GONDI(
        faction = Faction.GONDI,
        canActInSleep = true
    ),
    DOCTOR(
        faction = Faction.VILLAGER,
        canActInSleep = true
    ),
    DETECTIVE(
        faction = Faction.VILLAGER,
        canActInSleep = true
    ),
    ACCOMPLICE(
        faction = Faction.GONDI
    ),
    VILLAGER(
        faction = Faction.VILLAGER
    ),
    MODERATOR(
        faction = Faction.NEUTRAL,
        canAccuse = false,
        canVote = false
    );
}
