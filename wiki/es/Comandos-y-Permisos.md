> 🌐 [English](../Commands-and-Permissions.md) · **Español**

# 🎮 Comandos y Permisos

BetterDeathMessages expone dos comandos: **`/deaths`** para todos, y **`/bdm`** para administradores.

Todas las respuestas de voz del plugin usan el prefijo de chat `◈ Deaths:` en carmesí oscuro (`#B33A3A`) — el alias híbrido de TTS-Studio porque el nombre completo del plugin tiene 19 caracteres. Los mensajes de muerte en sí son contenido, no voz del plugin, por lo que se envían **sin** el prefijo.

---

## 💬 Comandos

| Comando | Descripción | Permiso |
|---------|-------------|---------|
| `/deaths` | Muestra **tu propio** panel de estadísticas de muerte/kills | `bdm.use` |
| `/deaths <jugador>` | Muestra el panel de estadísticas de otro jugador (soporta jugadores desconectados en caché) | `bdm.use` |
| `/bdm reload` | Recarga en caliente `messages.yml` (sin reinicio) | `bdm.admin` |

### `/deaths [jugador]`

Renderiza un panel de estadísticas con estilo MiniMessage:

```
◈ Deaths: ══ Stats de Steve ══
          Muertes totales: 47
          Asesinatos:      89
          KDR:             1.89
          Racha actual:    3
          Racha máxima:    12
```

Ejecuta sin argumentos para ver tus propias estadísticas. La consola siempre debe proporcionar un argumento `<jugador>`.

### `/bdm reload`

Recarga `messages.yml` desde disco y reconstruye el selector de plantillas interno. Úsalo después de editar plantillas, causas de transmisión o el mensaje de primera muerte del día.

```
◈ Deaths: ✓ BetterDeathMessages recargado.
```

> ⚠️ **Advertencia:** La recarga **no** borra `deaths.json` — las estadísticas persisten entre recargas y reinicios.

---

## 🪧 Transmisiones de Muerte (Stats en Hover)

Los mensajes de muerte emitidos en `PlayerDeathEvent` son **el contenido en sí** — no van prefijados con `◈ Deaths:`. La transmisión es el lore.

Cuando la causa de muerte figura en `global-broadcast.enabled-causes` (por defecto: `ENDER_DRAGON`, `WITHER`, `pvp`) **y** `global-broadcast.hover-stats: true`, la transmisión lleva un panel de hover con las estadísticas de por vida de la víctima:

```
[Pasando el cursor por la transmisión de muerte de Steve...]
┌─────────────────────────┐
│ Steve — Stats           │
│ Muertes totales: 47     │
│ Asesinatos:      89     │
│ KDR:             1.89   │
│ Racha máxima:    12     │
└─────────────────────────┘
```

Esto usa la API `HoverEvent.showText` de Adventure y funciona en cualquier cliente que soporte componentes de chat modernos (1.16+ vainilla, todas las versiones soportadas por Paper).

---

## 🔑 Permisos

### Permisos de Jugador

| Permiso | Descripción | Por Defecto |
|---------|-------------|-------------|
| `bdm.use` | Usar `/deaths` y `/deaths <jugador>` | `true` (todos los jugadores) |

### Permisos de Administrador

| Permiso | Descripción | Por Defecto |
|---------|-------------|-------------|
| `bdm.admin` | Usar `/bdm reload` | `op` |

> 📝 **Nota:** Ningún permiso controla las transmisiones de muerte en sí — son una función pasiva activada por `PlayerDeathEvent`. Para suprimir una categoría, edita las plantillas o `global-broadcast.enabled-causes` en `messages.yml`.

---

## 🛠️ Ejemplos con LuckPerms

```bash
# Dar al grupo staff acceso a la recarga
/lp group staff permission set bdm.admin true

# Ocultar /deaths al grupo guest
/lp group guest permission set bdm.use false

# Anulación por usuario
/lp user Admin permission set bdm.admin true
```

---

## 📋 Tabla Resumen de Permisos

| Permiso | Jugador | VIP | Staff | OP |
|---------|---------|-----|-------|----|
| `bdm.use` | ✅ | ✅ | ✅ | ✅ |
| `bdm.admin` | ❌ | ❌ | ✅ | ✅ |

---

🏠 [Inicio](Home.md) · 📦 [Instalación](Instalacion.md) · ⚙️ [Configuración](Configuracion.md) · 📊 [PlaceholderAPI](PlaceholderAPI.md)
