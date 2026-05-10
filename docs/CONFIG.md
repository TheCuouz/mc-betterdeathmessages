# Configuration Reference

## messages.yml

### Message categories

| Key | Trigger |
|-----|---------|
| `messages.fall` | Fall damage |
| `messages.lava` | Lava, fire, or hot-floor |
| `messages.drown` | Drowning |
| `messages.void` | Void |
| `messages.pvp` | Killed by another player |
| `messages.mob.<TYPE>` | Killed by specific mob (e.g. `ZOMBIE`, `ENDER_DRAGON`) |
| `messages.mob.DEFAULT` | Fallback for mobs with no specific entry |
| `messages.unknown` | Any other cause |

Each category is a list — one entry is picked at random.

### Template placeholders

| Placeholder | Value |
|-------------|-------|
| `<player>` | Victim's name |
| `<killer>` | Killer's name (pvp only) |
| `<weapon>` | Killer's held item display name |
| `<mob>` | Mob type formatted (e.g. "Zombie") |
| `<distance>` | Fall distance in blocks |
| `<biome>` | Biome name at death location |

All templates support full MiniMessage tags (`<red>`, `<gold>`, `<gradient:...>`, etc.).

### global-broadcast

```yaml
global-broadcast:
  enabled-causes: [ENDER_DRAGON, WITHER, pvp]
  hover-stats: true
```

Deaths from these causes get hover stats attached to the message. Values are mob type names or the literal `pvp`.

### first-death-of-day

```yaml
first-death-of-day:
  enabled: true
  message: "<gold>¡<player> tiene el honor de morir primero hoy!"
```

Resets automatically at server midnight (based on server's `LocalDate`).
