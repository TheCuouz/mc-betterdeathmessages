# 📊 PlaceholderAPI Integration

BetterDeathMessages registers its own `bdm` expansion on enable — no separate ecloud download required. The placeholders work in any PAPI-compatible plugin: scoreboards, TAB lists, chat formats, HUDs, holograms, anywhere.

---

## ⚙️ Setup

### 1. Install PlaceholderAPI

Drop [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) in `plugins/`, restart the server.

### 2. That's it

On enable, BetterDeathMessages detects PAPI and registers automatically. You'll see `· PAPI ✓` in the console banner:

```
21 death message templates loaded · PAPI ✓ · ready in 84ms
```

### 3. Verify

```
/papi parse <yourname> %bdm_kills%
# Returns: 0  (or your actual kill count)
```

> ⚠️ **Warning:** If PlaceholderAPI is missing or loads after BetterDeathMessages, placeholders will render literally as `%bdm_kills%`. Run `/papi reload` after installing PAPI mid-session.

---

## 📋 Placeholder Reference

| Placeholder | Returns | Example Output |
|-------------|---------|----------------|
| `%bdm_kills%` | Lifetime player kills | `89` |
| `%bdm_deaths_total%` | Lifetime deaths (all causes) | `47` |
| `%bdm_kdr%` | Kill / death ratio, 2 decimal places (divisor floored to 1) | `1.89` |
| `%bdm_killstreak%` | Current consecutive kills (resets on death) | `3` |

### Detailed Descriptions

#### `%bdm_kills%`
Total kills the player has registered across the server's lifetime. Increments whenever the player is the killer in `PlayerDeathEvent` (direct hit or projectile).

#### `%bdm_deaths_total%`
Total deaths across all categories — fall, lava, drowning, void, PvP, mob, unknown. The per-category breakdown is tracked internally (`deathsByCategory`) but is **not** exposed as a placeholder; query it via the [Developer API](#) if needed.

#### `%bdm_kdr%`
Kill/death ratio, computed as `totalKills / max(1, totalDeaths)` and formatted to 2 decimal places. The `max(1, …)` clamp prevents division-by-zero for fresh players.

| Kills | Deaths | `%bdm_kdr%` |
|-------|--------|-------------|
| 0 | 0 | `0.00` |
| 10 | 0 | `10.00` |
| 89 | 47 | `1.89` |
| 1 | 1 | `1.00` |

#### `%bdm_killstreak%`
The player's **current** kill streak — increments on every kill, resets to `0` on death. For the all-time best, the data is tracked internally as `longestKillStreak` but is not yet exposed as a placeholder.

---

## 💡 Usage Examples

### Scoreboard line (FeatherBoard / AnimatedScoreboard)

```yaml
lines:
  - "&7Kills:  &a%bdm_kills%"
  - "&7Deaths: &c%bdm_deaths_total%"
  - "&7KDR:    &e%bdm_kdr%"
  - "&7Streak: &6%bdm_killstreak%"
```

### TAB Plugin (TAB by NEZNAMY)

```yaml
tablist-name: "%player_name% &8[&a%bdm_kdr%&8]"
header: "&6Server Top Hunter: &f%bdm_kills% kills"
```

### Chat format (EssentialsX / DeluxeChat)

```yaml
format: "{prefix}&7[&e%bdm_kdr%&7] {name}&7: {message}"
```

### Holographic Displays

```yaml
hologram:
  - "&6&l═══ Hunter Stats ═══"
  - "&7Kills:  &a%bdm_kills%"
  - "&7Deaths: &c%bdm_deaths_total%"
  - "&7KDR:    &e%bdm_kdr%"
```

---

## 🔗 Compatible Plugins

| Plugin | Works With BetterDeathMessages Placeholders |
|--------|---------------------------------------------|
| TAB by NEZNAMY | ✅ |
| FeatherBoard | ✅ |
| CMI | ✅ |
| AnimatedScoreboard | ✅ |
| EssentialsX (chat format) | ✅ |
| DeluxeChat | ✅ |
| HolographicDisplays | ✅ (via PAPI bridge) |
| DecentHolograms | ✅ |
| ChatControlRed | ✅ |

---

## 🧠 Implementation Notes

- The expansion identifier is `bdm`, registered via Adventure's `PlaceholderExpansion`.
- `persist()` returns `true`, so offline-player lookups work in plugins that support them (e.g. TAB's `@offline` placeholders).
- Stats are read from the in-memory `ConcurrentHashMap` in [`DeathStatsService`](../src/main/java/com/cristian/betterdeathmessages/service/DeathStatsService.java), which is hydrated from `deaths.json` on enable.
- Placeholders return an empty string for `null` players. For unknown keys, they return `null` (PAPI's signal to render the raw placeholder).

> 💡 **Tip:** Combine `%bdm_kdr%` with conditional placeholder plugins (e.g. PlaceholderAPI's `javascript` expansion) to award titles like "Veteran" at KDR ≥ 2.0.

---

🏠 [Home](Home) · 🎮 [Commands & Permissions](Commands-and-Permissions) · ⚙️ [Configuration](Configuration)
