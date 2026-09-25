# Changelog

## [1.1.7] — 2026-09-25

### Fixed
- **Cactus, lightning and other deaths no longer fall into "died in mysterious ways".** Damage
  dealt by the `/damage` command or by other plugins arrives with cause `CUSTOM`, so only the
  generic lines were used. The death type is now read from the damage itself (cactus,
  lightning, freezing, explosions, the Warden…) and each one gets its own message.
- **Arrows are credited to whoever shot them.** A skeleton's arrow used to say "slain by a
  Arrow"; it now uses the Skeleton, Stray or Bogged lines, and `{distance}` is how far away
  the shooter stood instead of how far the victim had fallen.
- **"an Iron sword", not "a Iron sword".** In English, the "a" before `{weapon}` and `{mob}`
  now changes to "an" when the name starts with a vowel.
- **Biomes read as names.** `{biome}` shows "Lukewarm Ocean" instead of `lukewarm_ocean`.
- Sweet berry bushes no longer use the cactus lines.

## [1.1.5] — 2026-09-20

### Changed
- **Menu titles no longer repeat the plugin name.** `◈ Plugin › Section` is now
  `◈ Section`: measured with the game's own font, the name alone ate more than half
  of the 160 px a chest title has, so any real section came out cut off with an
  ellipsis. The diamond keeps the plugin's colour, and anything still too wide is
  trimmed to fit.

---

## [1.1.6] — 2026-09-21

### Fixed
- **Numbers no longer follow the server's system language.** `String.format` was used
  without a `Locale`, so the very same jar printed `$12.50` on an English machine and
  `$12,50` on a Spanish or French one, and the thousands separator flipped with it. Every
  player-facing number is written with `Locale.ROOT` now, and a test keeps it that way.

---

## [1.1.4] — 2026-09-12

### Fixed
- 🚨 **The plugin did not start on a server without Vault.** `VaultHook.setup()`
  read `Economy.class` with no guard, and resolving that class without Vault
  installed is a `NoClassDefFoundError` in `onEnable`: BetterDeathMessages
  disabled itself on any server that never asked for Vault. Vault is listed as a
  **soft** dependency and the resource sells it as optional — it was mandatory.
  The lookup now happens only after `isPluginEnabled("Vault")`.
- **How it came out:** booting every plugin of the suite on a bare Paper 1.21.11
  with none of the optional dependencies installed. It is the same family as the
  one caught in TradeForge the same day, and `paper-test` hides both because it
  has Vault and PlaceholderAPI installed.

---


All notable changes to BetterDeathMessages are documented here.

## [1.1.2] — 2026-08-26

### Changed
- **Default interface language is English** (`language: en`), and every `config.yml` comment is
  now in English. The build published on BuiltByBit was still shipping the Spanish default.


## [1.1.0] - 2026-05-14

### Added
- **Last Words** — captures each player's last public chat message and
  injects it into death templates via the new `{last_words}` token.
- `LastWordsCache` with configurable TTL (default 60s) and periodic
  janitor that purges expired entries every minute.
- `ChatCaptureListener` for `AsyncChatEvent` (priority `MONITOR`,
  `ignoreCancelled = true`) with a conservative public-chat policy:
  if any other plugin has narrowed the audience (whispers, channel
  chat, party chat, etc.), the message is silently discarded.
- New `last-words` section in `config.yml` (`enabled`, `cache-seconds`,
  `fallback`).
- Banner hook label `LastWords` shown on `onEnable` when the feature
  is active.

### Privacy
- Last Words only captures messages whose final audience is the full
  online roster + console. Whispers, `/msg`, and channel-scoped chat
  (e.g. ChatChannels `#trade`) never enter the cache.
- Cached messages are dropped after `cache-seconds` and on `onDisable`
  (the cache lives only in memory).

### Changed
- `MessagePicker` now performs `{last_words}` substitution. Player
  chat content is escaped for MiniMessage (`<` → `\<`) before
  insertion so user input cannot break or inject formatting.

## [1.0.0] - 2026-05-10

### Added
- Initial release with 110+ narrative death message templates across
  17 cause categories (fall, lava, fire, drown, void, explosion, magic,
  lightning, and more) plus 42 per-mob categories with a DEFAULT fallback.
- Random template selection per category.
- Hover stats on notable deaths (ENDER_DRAGON, WITHER, pvp) showing
  total deaths, kills, KDR, and longest killstreak.
- First-death-of-day broadcast with automatic midnight reset.
- Persistent per-player stats via Gson (`deaths.json`).
- `/deaths [player]` command.
- `/bdm reload` admin command.
- PlaceholderAPI expansion (`%bdm_deaths_total%`, `%bdm_kills%`,
  `%bdm_kdr%`, `%bdm_killstreak%`).
- bStats metrics (plugin ID 31359).
