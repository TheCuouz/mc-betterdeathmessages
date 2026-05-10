# BetterDeathMessages

Replaces vanilla death messages with configurable narrative variants using MiniMessage.

## Features

- Random narrative death messages per cause (fall, lava, drown, void, pvp, per-mob)
- Per-mob-type messages with DEFAULT fallback
- Hover stats on notable deaths: victim's total deaths, kills, KDR, killstreak
- Global broadcast for notable kills (ENDER_DRAGON, WITHER, pvp) with hover text
- First-death-of-day announcement (auto-resets at midnight)
- Persistent per-player stats in `deaths.json` (Gson)
- `/deaths [player]` — view stats
- `/bdm reload` — hot-reload messages.yml
- PlaceholderAPI support

## Requirements

- Paper 1.21.4+
- Java 21+
- Optional: PlaceholderAPI

## Installation

Drop `betterdeathmessages-1.0.0.jar` into `plugins/` and restart.

## Documentation

- [CONFIG.md](docs/CONFIG.md)
- [PERMISSIONS.md](docs/PERMISSIONS.md)
- [PLACEHOLDERS.md](docs/PLACEHOLDERS.md)
- [CHANGELOG.md](docs/CHANGELOG.md)
