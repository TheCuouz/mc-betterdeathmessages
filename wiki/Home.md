> 🌐 **English** · [Español](es/Home.md)

# 💀 BetterDeathMessages Wiki

```
 ██████╗ ███████╗████████╗████████╗███████╗██████╗     ██████╗ ███████╗ █████╗ ████████╗██╗  ██╗
 ██╔══██╗██╔════╝╚══██╔══╝╚══██╔══╝██╔════╝██╔══██╗    ██╔══██╗██╔════╝██╔══██╗╚══██╔══╝██║  ██║
 ██████╔╝█████╗     ██║      ██║   █████╗  ██████╔╝    ██║  ██║█████╗  ███████║   ██║   ███████║
 ██╔══██╗██╔══╝     ██║      ██║   ██╔══╝  ██╔══██╗    ██║  ██║██╔══╝  ██╔══██║   ██║   ██╔══██║
 ██████╔╝███████╗   ██║      ██║   ███████╗██║  ██║    ██████╔╝███████╗██║  ██║   ██║   ██║  ██║
 ╚═════╝ ╚══════╝   ╚═╝      ╚═╝   ╚══════╝╚═╝  ╚═╝    ╚═════╝ ╚══════╝╚═╝  ╚═╝   ╚═╝   ╚═╝  ╚═╝
                                        M E S S A G E S
```

**Narrative, MiniMessage-styled death messages with persistent kill/death stats for Paper 1.21.x.**

Part of the **TTS-Studio plugin suite** — built around a shared SDK for consistent console banners, chat prefixes, and brand presence across every plugin you run.

> ◈ **Deaths** — the plugin's chat voice. Color: dark crimson `#B33A3A`.
> Death broadcasts themselves are *content*, not plugin voice, so they ship **without** the prefix.

---

## ✨ Feature Highlights

| Feature | Description |
|---------|-------------|
| 📜 **21+ Death Templates** | Mob-aware, cause-aware narrative messages — gravity, lava, drowning, void, PvP, and per-mob variants |
| 🎨 **MiniMessage Styling** | Full color, gradient, and decoration support via the Adventure API |
| 📊 **Persistent Kill/Death Stats** | `totalKills`, `totalDeaths`, `currentKillStreak`, `longestKillStreak`, per-category death breakdown |
| 🔍 **Hover Stats on Broadcasts** | Hover any global broadcast to see the victim's full panel |
| 📣 **Global Broadcasts** | Configurable per-cause — Wither, Ender Dragon, and PvP kills broadcast server-wide by default |
| 🌅 **First Death of the Day** | Prominent global announcement when the first player dies each server day |
| 🧮 **PlaceholderAPI Hook** | `%bdm_kills%`, `%bdm_deaths_total%`, `%bdm_kdr%`, `%bdm_killstreak%` |
| 🛠️ **Hot Reload** | `/bdm reload` re-reads `messages.yml` without a restart |
| 💾 **JSON Persistence** | Stats survive restarts — stored in `deaths.json` |

---

## ⚡ Quick Start

```bash
# 1. Drop the jar
cp betterdeathmessages-1.0.0.jar plugins/

# 2. Start the server — messages.yml is auto-generated
# 3. (Optional) edit plugins/BetterDeathMessages/messages.yml
# 4. Apply changes:
/bdm reload
```

That's it. The plugin works out of the box with 21+ pre-written templates.

---

## 🖥️ Console Banner

On enable, you'll see the TTS-Studio framed banner with plugin status:

```
╔════════════════════════════════════════════════════════════╗
║                       T T S - S T U D I O                  ║
║                                                            ║
║   BetterDeathMessages v1.0.0                               ║
║   21 death message templates loaded · PAPI ✓ · ready in 84ms ║
╚════════════════════════════════════════════════════════════╝
```

The status line reports the number of templates parsed from `messages.yml`, whether PlaceholderAPI was detected, and the total enable duration.

---

## 🧭 Wiki Navigation

| Page | Contents |
|------|----------|
| 📦 [Installation](Installation) | Requirements, JAR deployment, first-run checklist |
| 🎮 [Commands & Permissions](Commands-and-Permissions) | `/deaths`, `/bdm reload`, permission tree, hover-text feature |
| ⚙️ [Configuration](Configuration) | Full `messages.yml` reference, template syntax, MiniMessage primer |
| 📊 [PlaceholderAPI](PlaceholderAPI) | All `%bdm_*%` placeholders with scoreboard/TAB examples |

---

## 📋 Requirements

| Requirement | Version | Notes |
|-------------|---------|-------|
| Paper | 1.21.x | Spigot is **not** supported (uses `event.deathMessage(Component)`) |
| Java | 21+ | Required |
| PlaceholderAPI | 2.11.6+ | Optional — enables `%bdm_*%` placeholders |

---

*BetterDeathMessages — by **TTS-Studio** · Death is the new lore.*
