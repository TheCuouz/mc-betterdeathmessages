> 🌐 [English](../PlaceholderAPI.md) · **Español**

# 📊 Integración con PlaceholderAPI

BetterDeathMessages registra su propia expansión `bdm` al activarse — no se requiere ninguna descarga adicional de ecloud. Los placeholders funcionan en cualquier plugin compatible con PAPI: marcadores, listas TAB, formatos de chat, HUDs, hologramas, en cualquier lugar.

---

## ⚙️ Configuración

### 1. Instala PlaceholderAPI

Coloca [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) en `plugins/` y reinicia el servidor.

### 2. Eso es todo

Al activarse, BetterDeathMessages detecta PAPI y se registra automáticamente. Verás `· PAPI ✓` en el banner de consola:

```
21 death message templates loaded · PAPI ✓ · ready in 84ms
```

### 3. Verificar

```
/papi parse <tunombre> %bdm_kills%
# Devuelve: 0  (o tu conteo real de kills)
```

> ⚠️ **Advertencia:** Si PlaceholderAPI no está instalado o carga después de BetterDeathMessages, los placeholders se renderizarán literalmente como `%bdm_kills%`. Ejecuta `/papi reload` después de instalar PAPI a mitad de sesión.

---

## 📋 Referencia de Placeholders

| Placeholder | Devuelve | Ejemplo de Salida |
|-------------|----------|-------------------|
| `%bdm_kills%` | Kills de jugadores de por vida | `89` |
| `%bdm_deaths_total%` | Muertes de por vida (todas las causas) | `47` |
| `%bdm_kdr%` | Ratio kill/muerte, 2 decimales (divisor mínimo 1) | `1.89` |
| `%bdm_killstreak%` | Kills consecutivos actuales (se reinicia al morir) | `3` |

### Descripciones Detalladas

#### `%bdm_kills%`
Total de kills que el jugador ha registrado durante la vida del servidor. Se incrementa cada vez que el jugador es el asesino en `PlayerDeathEvent` (golpe directo o proyectil).

#### `%bdm_deaths_total%`
Total de muertes en todas las categorías — caída, lava, ahogamiento, vacío, PvP, mob, desconocido. El desglose por categoría se rastrea internamente (`deathsByCategory`) pero **no** se expone como placeholder; consúltalo mediante la [API de Desarrollador](#) si lo necesitas.

#### `%bdm_kdr%`
Ratio kill/muerte, calculado como `totalKills / max(1, totalDeaths)` y formateado a 2 decimales. El límite `max(1, …)` evita la división por cero para jugadores nuevos.

| Kills | Muertes | `%bdm_kdr%` |
|-------|---------|-------------|
| 0 | 0 | `0.00` |
| 10 | 0 | `10.00` |
| 89 | 47 | `1.89` |
| 1 | 1 | `1.00` |

#### `%bdm_killstreak%`
La racha de kills **actual** del jugador — se incrementa con cada kill y se reinicia a `0` al morir. Para el mejor histórico de por vida, los datos se rastrean internamente como `longestKillStreak` pero aún no están expuestos como placeholder.

---

## 💡 Ejemplos de Uso

### Línea de marcador (FeatherBoard / AnimatedScoreboard)

```yaml
lines:
  - "&7Kills:  &a%bdm_kills%"
  - "&7Deaths: &c%bdm_deaths_total%"
  - "&7KDR:    &e%bdm_kdr%"
  - "&7Streak: &6%bdm_killstreak%"
```

### Plugin TAB (TAB by NEZNAMY)

```yaml
tablist-name: "%player_name% &8[&a%bdm_kdr%&8]"
header: "&6Server Top Hunter: &f%bdm_kills% kills"
```

### Formato de chat (EssentialsX / DeluxeChat)

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

## 🔗 Plugins Compatibles

| Plugin | Compatible con Placeholders de BetterDeathMessages |
|--------|-----------------------------------------------------|
| TAB by NEZNAMY | ✅ |
| FeatherBoard | ✅ |
| CMI | ✅ |
| AnimatedScoreboard | ✅ |
| EssentialsX (formato de chat) | ✅ |
| DeluxeChat | ✅ |
| HolographicDisplays | ✅ (mediante puente PAPI) |
| DecentHolograms | ✅ |
| ChatControlRed | ✅ |

---

## 🧠 Notas de Implementación

- El identificador de expansión es `bdm`, registrado mediante `PlaceholderExpansion` de Adventure.
- `persist()` devuelve `true`, por lo que las búsquedas de jugadores desconectados funcionan en plugins que las soportan (p. ej., placeholders `@offline` de TAB).
- Los placeholders se leen desde el `ConcurrentHashMap` en memoria de [`DeathStatsService`](../src/main/java/com/cristian/betterdeathmessages/service/DeathStatsService.java), que se hidrata desde `deaths.json` al activarse.
- Los placeholders devuelven una cadena vacía para jugadores `null`. Para claves desconocidas, devuelven `null` (la señal de PAPI para renderizar el placeholder literal).

> 💡 **Consejo:** Combina `%bdm_kdr%` con plugins de placeholders condicionales (p. ej., la expansión `javascript` de PlaceholderAPI) para otorgar títulos como "Veterano" con un KDR ≥ 2.0.

---

🏠 [Inicio](Home.md) · 🎮 [Comandos y Permisos](Comandos-y-Permisos.md) · ⚙️ [Configuración](Configuracion.md)
