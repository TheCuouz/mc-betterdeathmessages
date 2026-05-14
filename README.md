# BetterDeathMessages

Narrative death messages for Paper 1.21. Replaces vanilla one-liners with a configurable library of MiniMessage templates split by cause (fall, lava, drown, void, pvp, per-mob), and adds hover stats, daily broadcasts, persistent stats, and — new in 1.1.0 — **Last Words**.

## Features

- **21 default templates** across categories: `fall`, `lava`, `drown`, `void`, `pvp`, and per-mob (`ZOMBIE`, `SKELETON`, `CREEPER`, `SPIDER`, `ENDER_DRAGON`, `WITHER`, with `DEFAULT` fallback)
- **Random template selection** per category — no two deaths read the same
- **Last Words** — the player's last public chat message is woven into their death message via the `{last_words}` token
- **Hover stats** on notable deaths (ENDER_DRAGON, WITHER, pvp) showing total deaths, kills, KDR, and longest killstreak
- **First-death-of-day broadcast** with automatic midnight reset
- **Persistent player stats** in `deaths.json` (Gson)
- `/deaths [player]` — view a player's stats; `/bdm reload` — hot-reload `messages.yml`
- **PlaceholderAPI** support, **bStats** metrics

## Last Words

New in **1.1.0**. The plugin remembers each player's most recent public chat line for a short, configurable window. When that player dies, the `{last_words}` token in their death template is replaced by what they said.

```yaml
# messages.yml
fall:
  - "<gray><player> cayó. Últimas palabras: <italic>\"{last_words}\"</italic></gray>"
```

> **Privacy.** Last Words only captures chat that reaches the full online roster — i.e. true public chat. Messages routed through ChatChannels or other channel plugins (`#trade`, staff chat, party chat), whispers, and `/msg` **never** enter the cache. The listener runs at `MONITOR` priority with `ignoreCancelled = true`, so it sees the final audience after every other plugin has had its say. Cached messages are dropped after `cache-seconds` (default 60s) and the cache lives only in memory — nothing is written to disk.

Tune it in `config.yml`:

```yaml
last-words:
  enabled: true        # set to false to disable capture entirely
  cache-seconds: 60    # how long a message remains usable as last words
  fallback: ""         # substituted when no recent message is cached
```

## Requirements

| Dependency | Version | Scope |
|---|---|---|
| Paper | 1.21+ | Required |
| Java | 21+ | Required |
| PlaceholderAPI | 2.11+ | Optional |

## Installation

1. Drop `betterdeathmessages-1.1.0.jar` into your `plugins/` folder
2. Restart the server — default `config.yml` and `messages.yml` are generated
3. Edit `plugins/BetterDeathMessages/messages.yml` to add your own templates
4. `/bdm reload` applies changes without restart

## Commands & Permissions

| Command | Description | Permission | Default |
|---|---|---|---|
| `/deaths [player]` | View death stats | `bdm.use` | true |
| `/bdm reload` | Reload `messages.yml` and `config.yml` | `bdm.admin` | op |

## Available tokens

These are substituted inside any template in `messages.yml`:

| Token | Replaced with |
|---|---|
| `<player>` | Victim's name |
| `<killer>` | Killer's name (PvP only; empty otherwise) |
| `<weapon>` | Weapon used (custom display name if set) |
| `<mob>` | Mob type (mob deaths only) |
| `<distance>` | Fall distance in blocks |
| `<biome>` | Biome at the death location |
| `{last_words}` | Player's last public chat message (or `fallback`) |

## Configuration

```yaml
# messages.yml — excerpt
messages:
  pvp:
    - "<red><player></red> fue derrotado por <gold><killer></gold> usando <aqua><weapon></aqua>"
  fall:
    - "<gray><player> cayó. Últimas palabras: <italic>\"{last_words}\"</italic></gray>"
  mob:
    ENDER_DRAGON:
      - "<dark_purple><player></dark_purple> osó desafiar al dragón... y perdió"
    DEFAULT:
      - "<red><player></red> fue derrotado por un <mob>"

global-broadcast:
  enabled-causes: [ENDER_DRAGON, WITHER, pvp]
  hover-stats: true

first-death-of-day:
  enabled: true
  message: "<gold>¡<player></gold> tiene el honor de morir primero hoy!"
```

## PlaceholderAPI

| Placeholder | Value |
|---|---|
| `%bdm_deaths_total%` | Total deaths |
| `%bdm_kills%` | Total kills |
| `%bdm_kdr%` | Kill/death ratio |
| `%bdm_killstreak%` | Current killstreak |

## Documentation

Full reference in [docs/](docs/):
[CONFIG.md](docs/CONFIG.md) · [PERMISSIONS.md](docs/PERMISSIONS.md) · [PLACEHOLDERS.md](docs/PLACEHOLDERS.md) · [CHANGELOG.md](CHANGELOG.md)
