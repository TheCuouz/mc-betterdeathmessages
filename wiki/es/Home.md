> 🌐 [English](../Home.md) · **Español**

# 💀 Wiki de BetterDeathMessages

```
 ██████╗ ███████╗████████╗████████╗███████╗██████╗     ██████╗ ███████╗ █████╗ ████████╗██╗  ██╗
 ██╔══██╗██╔════╝╚══██╔══╝╚══██╔══╝██╔════╝██╔══██╗    ██╔══██╗██╔════╝██╔══██╗╚══██╔══╝██║  ██║
 ██████╔╝█████╗     ██║      ██║   █████╗  ██████╔╝    ██║  ██║█████╗  ███████║   ██║   ███████║
 ██╔══██╗██╔══╝     ██║      ██║   ██╔══╝  ██╔══██╗    ██║  ██║██╔══╝  ██╔══██║   ██║   ██╔══██║
 ██████╔╝███████╗   ██║      ██║   ███████╗██║  ██║    ██████╔╝███████╗██║  ██║   ██║   ██║  ██║
 ╚═════╝ ╚══════╝   ╚═╝      ╚═╝   ╚══════╝╚═╝  ╚═╝    ╚═════╝ ╚══════╝╚═╝  ╚═╝   ╚═╝   ╚═╝  ╚═╝
                                        M E S S A G E S
```

**Mensajes de muerte narrativos con estilo MiniMessage y estadísticas persistentes de kills/muertes para Paper 1.21.x.**

Parte de la **suite de plugins TTS-Studio** — construida sobre un SDK compartido para banners de consola, prefijos de chat y presencia de marca consistentes en todos los plugins que ejecutes.

> ◈ **Deaths** — la voz del plugin en el chat. Color: carmesí oscuro `#B33A3A`.
> Los mensajes de muerte en sí son *contenido*, no voz del plugin, por lo que se envían **sin** el prefijo.

---

## ✨ Características Destacadas

| Característica | Descripción |
|----------------|-------------|
| 📜 **21+ Plantillas de Muerte** | Mensajes narrativos conscientes del mob y la causa — gravedad, lava, ahogamiento, vacío, PvP y variantes por mob |
| 🎨 **Estilo MiniMessage** | Soporte completo de color, gradiente y decoración mediante la Adventure API |
| 📊 **Estadísticas Persistentes de Kills/Muertes** | `totalKills`, `totalDeaths`, `currentKillStreak`, `longestKillStreak`, desglose de muertes por categoría |
| 🔍 **Stats en Hover de Transmisiones** | Pasa el cursor por cualquier transmisión global para ver el panel completo de la víctima |
| 📣 **Transmisiones Globales** | Configurables por causa — Wither, Ender Dragon y kills PvP transmiten por defecto a todo el servidor |
| 🌅 **Primera Muerte del Día** | Anuncio global prominente cuando el primer jugador muere cada día de servidor |
| 🧮 **Hook de PlaceholderAPI** | `%bdm_kills%`, `%bdm_deaths_total%`, `%bdm_kdr%`, `%bdm_killstreak%` |
| 🛠️ **Recarga en Caliente** | `/bdm reload` relee `messages.yml` sin reiniciar |
| 💾 **Persistencia JSON** | Las estadísticas sobreviven a los reinicios — almacenadas en `deaths.json` |

---

## ⚡ Inicio Rápido

```bash
# 1. Copia el jar
cp betterdeathmessages-1.0.0.jar plugins/

# 2. Inicia el servidor — messages.yml se genera automáticamente
# 3. (Opcional) edita plugins/BetterDeathMessages/messages.yml
# 4. Aplica los cambios:
/bdm reload
```

Eso es todo. El plugin funciona de inmediato con 21+ plantillas preescritas.

---

## 🖥️ Banner de Consola

Al activarse, verás el banner enmarcado de TTS-Studio con el estado del plugin:

```
╔════════════════════════════════════════════════════════════╗
║                       T T S - S T U D I O                  ║
║                                                            ║
║   BetterDeathMessages v1.0.0                               ║
║   21 death message templates loaded · PAPI ✓ · ready in 84ms ║
╚════════════════════════════════════════════════════════════╝
```

La línea de estado informa el número de plantillas analizadas desde `messages.yml`, si PlaceholderAPI fue detectado y la duración total de activación.

---

## 🧭 Navegación del Wiki

| Página | Contenido |
|--------|-----------|
| 📦 [Instalación](Instalacion.md) | Requisitos, despliegue del JAR, lista de verificación del primer arranque |
| 🎮 [Comandos y Permisos](Comandos-y-Permisos.md) | `/deaths`, `/bdm reload`, árbol de permisos, función de hover |
| ⚙️ [Configuración](Configuracion.md) | Referencia completa de `messages.yml`, sintaxis de plantillas, introducción a MiniMessage |
| 📊 [PlaceholderAPI](PlaceholderAPI.md) | Todos los placeholders `%bdm_*%` con ejemplos de marcador y TAB |

---

## 📋 Requisitos

| Requisito | Versión | Notas |
|-----------|---------|-------|
| Paper | 1.21.x | Spigot **no** está soportado (usa `event.deathMessage(Component)`) |
| Java | 21+ | Requerido |
| PlaceholderAPI | 2.11.6+ | Opcional — habilita los placeholders `%bdm_*%` |

---

*BetterDeathMessages — by **TTS-Studio** · La muerte es el nuevo lore.*
