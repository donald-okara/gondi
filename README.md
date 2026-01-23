<p align="center">
  <img src="shared/resources/src/commonMain/composeResources/drawable/app_icon.png" 
       alt="Gondi Banner" 
       width="180" />
</p>

<p align="center">
  <a href="https://play.google.com/store/apps/details?id=ke.don.gondi">
    <img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png" height="80"/>
  </a>
</p>

<h1 align="center">Gondi</h1>

Gondi is a rendition of the group game "Mafia" where there are 2 Gondis (Mafia members), one accomplice, one detective, a doctor and the villagers

Consult the [setup guide](docs/GET_STARTED.md) on how to get started.

## Roles
**Gondi (x2)** - In the beginning of every round (the villagers asleep), they kill a villager each

**Doctor**- In the Sleep phase, they can save any player or themselves just not twice in a row. They also can't save someone already eliminated

**Detective**- In each sleep, they can inquire on the identity of one player. They can share the identity if they feel like (Can only be in the game if there is an accomplise)

**Accomplice** - Runs interference. They throw everyone off the real Gondi's trail (they don't know who the gondis are either) (Can only be in the game if there is a detective). They win if at least one gondi is left standing

**Villager**-  Votes on who the Gondis are after every sleep round

**Narrator/Moderator** - Moderates the game

- All Gondis are dead → Villagers win.

- Gondis equal or outnumber villagers → Gondis win.
## Phases
### 1. Sleep
Here Gondi's, Doctors and Detectives do their work

### 2. Town hall
The villagers discuss who the Gondis are. If **anyone** accuses a Gondi and **anyone** else seconds, we move to court

### 3. Court
The Accused states their case and then everyone votes on eviction. Then on eviction, They get eliminated and their identity is revealed

Back to sleep

## Game Logic
### a. State machine diagram
```
┌─────────────────────────────┐
│          LOBBY              │
│  (Waiting for all players)  │
└──────────────┬──────────────┘
              │
              ▼
┌──────────────────────────────┐
│            SLEEP             │
│  Gondi kills → Doctor saves  │
│  Detective investigates      │
└──────────────┬───────────────┘
              │
              │  [Night actions resolved]
              ▼
┌──────────────────────────────┐
│          TOWN HALL           │
│  Discussion + Accusations    │
│  If accusation & second →    │
│            COURT             │
└──────┬───────────┬───────────┘
      │           │
      │ no accuse │
      ▼           │
┌────────────────┐ │
│   Next SLEEP   │ │
└────────────────┘ │
                  │
                  ▼
┌──────────────────────────────┐
│            COURT             │
│  Accused defends → Vote      │
│  If guilty → Eliminate       │
└──────────────┬───────────────┘
              │
              │  [Check win condition]
              ▼
┌──────────────────────────────┐
│   Win?                       │
│  ┌──────────────┬──────────┐ │
│  │ Yes          │ No       │ │
│  ▼              ▼          │ │
│ GAME OVER     Next SLEEP   │ │
└──────────────────────────────┘


```

### b. Database-Oriented State Flow

```
[Player Intent]
    │
    ▼
┌────────────────────┐
│ GameEngine.reduce()│
│ (Applies rules +   │
│  validates actions)│
└────────┬───────────┘
        │
        ▼
┌────────────────────┐
│   Local Database   │
│  (Room/SQLDelight) │
│ game_state table   │
│ player_state table │
└────────┬───────────┘
        │ emits
        ▼
┌────────────────────┐
│   Flow<GameState>  │
│  Observed by UI    │
└────────────────────┘

```

### c. Tables
| Table        | Key Columns                                                                                                      | Notes                                    |
| ------------ | ---------------------------------------------------------------------------------------------------------------- | ---------------------------------------- |
| `game_state` | `id`, `phase`, `round`, `pending_kills`, `last_saved_player_id`, `accused_player_id`, `reveal_eliminated_player` | Single-row table — current game metadata |
| `players`    | `id`, `name`, `role`, `is_alive`, `known_identities`, `last_action`                                              | Holds per-player info                    |
| `votes`      | `voter_id`, `target_id`, `is_guilty`                                                                             | Reused each court phase                  |
