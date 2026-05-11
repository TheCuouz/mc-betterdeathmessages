# 🎮 Commands & Permissions

BetterDeathMessages exposes two commands: **`/deaths`** for everyone, and **`/bdm`** for admins.

All plugin-voice replies use the `◈ Deaths:` chat prefix in dark crimson (`#B33A3A`) — the TTS-Studio hybrid alias because the full plugin name is 19 characters long. Death broadcasts themselves are content, not plugin voice, so they ship **without** the prefix.

---

## 💬 Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/deaths` | Show **your own** death/kill stats panel | `bdm.use` |
| `/deaths <player>` | Show another player's stats panel (cached offline players supported) | `bdm.use` |
| `/bdm reload` | Hot-reload `messages.yml` (no restart) | `bdm.admin` |

### `/deaths [player]`

Renders a MiniMessage-styled stats panel:

```
◈ Deaths: ══ Stats de Steve ══
          Muertes totales: 47
          Asesinatos:      89
          KDR:             1.89
          Racha actual:    3
          Racha máxima:    12
```

Run without arguments to see your own stats. Console must always supply a `<player>` argument.

### `/bdm reload`

Reloads `messages.yml` from disk and rebuilds the internal template picker. Use after editing templates, broadcast causes, or the first-death-of-day message.

```
◈ Deaths: ✓ BetterDeathMessages recargado.
```

> ⚠️ **Warning:** Reload does **not** clear `deaths.json` — stats persist across reloads and restarts.

---

## 🪧 Death Broadcasts (Hover Stats)

Death messages emitted on `PlayerDeathEvent` are **the content itself** — they're not prefixed with `◈ Deaths:`. The broadcast is the lore.

When the death cause is listed in `global-broadcast.enabled-causes` (default: `ENDER_DRAGON`, `WITHER`, `pvp`) **and** `global-broadcast.hover-stats: true`, the broadcast carries a hover-text panel with the victim's lifetime stats:

```
[Hovering Steve's death broadcast...]
┌─────────────────────────┐
│ Steve — Stats           │
│ Muertes totales: 47     │
│ Asesinatos:      89     │
│ KDR:             1.89   │
│ Racha máxima:    12     │
└─────────────────────────┘
```

This uses the Adventure `HoverEvent.showText` API and works on any client that supports modern chat components (1.16+ vanilla, all Paper-supported versions).

---

## 🔑 Permissions

### Player Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `bdm.use` | Use `/deaths` and `/deaths <player>` | `true` (all players) |

### Admin Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `bdm.admin` | Use `/bdm reload` | `op` |

> 📝 **Note:** No permission gates death broadcasts themselves — they're a passive feature triggered by `PlayerDeathEvent`. To suppress a category, edit the templates or `global-broadcast.enabled-causes` in `messages.yml`.

---

## 🛠️ LuckPerms Examples

```bash
# Give staff group access to reload
/lp group staff permission set bdm.admin true

# Hide /deaths from a guest group
/lp group guest permission set bdm.use false

# Per-user override
/lp user Admin permission set bdm.admin true
```

---

## 📋 Permission Summary Table

| Permission | Player | VIP | Staff | OP |
|------------|--------|-----|-------|----|
| `bdm.use` | ✅ | ✅ | ✅ | ✅ |
| `bdm.admin` | ❌ | ❌ | ✅ | ✅ |

---

🏠 [Home](Home) · 📦 [Installation](Installation) · ⚙️ [Configuration](Configuration) · 📊 [PlaceholderAPI](PlaceholderAPI)
