> 🌐 [English](../Installation.md) · **Español**

# 📦 Instalación

Esta página te guía paso a paso para poner **BetterDeathMessages** en funcionamiento en tu servidor Paper.

---

## 🔧 Requisitos

| Software | Versión Mínima | ¿Requerido? |
|----------|----------------|-------------|
| [Paper](https://papermc.io/) | **1.21.x** | ✅ Sí |
| Java | **21** | ✅ Sí |
| [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) | 2.11.6+ | ⚡ Opcional |

> ⚠️ **Advertencia:** BetterDeathMessages depende de la API `PlayerDeathEvent#deathMessage(Component)` de Paper. Spigot, CraftBukkit y Folia **no** están soportados.

> ⚠️ **Advertencia:** Java 21 es el mínimo. Ejecutarlo con Java 17 causará que el plugin falle al cargar con un `UnsupportedClassVersionError`.

---

## 🚀 Instalación Paso a Paso

### Paso 1 — Obtén el JAR

Compila desde el código fuente o descarga `betterdeathmessages-1.0.0.jar` desde la release oficial.

```bash
# Para compilar desde el código fuente:
git clone https://github.com/TheCuouz/mc-betterdeathmessages.git
cd mc-betterdeathmessages
mvn clean package
# El JAR estará en target/betterdeathmessages-1.0.0.jar
```

### Paso 2 — Despliega el JAR

```bash
cp betterdeathmessages-1.0.0.jar /tu/servidor/plugins/

# Tu directorio plugins/ debería contener ahora:
# plugins/
# ├── BetterDeathMessages-1.0.0.jar
# └── ...otros plugins...
```

### Paso 3 — Primer Arranque

Inicia o reinicia tu servidor. BetterDeathMessages genera su directorio de datos automáticamente:

```
plugins/BetterDeathMessages/
├── config.yml      ← idioma, últimas palabras, broadcasts, rachas, sonidos, radio
├── lang/
│   ├── es.yml       ← 110+ plantillas de muerte (español) — edita este archivo
│   └── en.yml       ← 110+ plantillas de muerte (inglés)
└── deaths.json     ← se crea automáticamente con la primera muerte; estadísticas persistentes
```

Deberías ver el banner de TTS-Studio en la consola:

```
╔════════════════════════════════════════════════════════════╗
║                       T T S - S T U D I O                  ║
║   BetterDeathMessages v1.0.0                               ║
║   21 death message templates loaded · PAPI ✓ · ready in 84ms ║
╚════════════════════════════════════════════════════════════╝
```

### Paso 4 — (Opcional) Instala PlaceholderAPI

Si quieres los placeholders `%bdm_*%` en marcadores, listas TAB o formatos de chat:

```
/papi reload
```

PlaceholderAPI detecta BetterDeathMessages automáticamente — no hay ninguna expansión separada que descargar. El plugin registra su propia expansión `bdm` al activarse.

### Paso 5 — (Opcional) Personaliza los Mensajes

Abre `plugins/BetterDeathMessages/messages.yml` y ajusta las plantillas a tu gusto. Consulta [Configuración](Configuracion.md) para la referencia completa.

Aplica los cambios con:

```
/bdm reload
```

No es necesario reiniciar el servidor.

---

## ✅ Lista de Verificación Post-Instalación

- [ ] El JAR está en `plugins/`
- [ ] El servidor se inició al menos una vez para generar `messages.yml`
- [ ] (Opcional) PlaceholderAPI instalado y ejecutado `/papi reload`
- [ ] (Opcional) Plantillas personalizadas en `messages.yml`
- [ ] `/bdm reload` funciona para administradores (permiso `bdm.admin` u OP)
- [ ] `/deaths <jugador>` muestra el panel de estadísticas

---

## 🔄 Actualizar BetterDeathMessages

1. Detén el servidor
2. Reemplaza el JAR antiguo por el nuevo — **conserva** `messages.yml` y `deaths.json`
3. Inicia el servidor
4. (Si el registro de cambios menciona nuevas plantillas) fusiona las entradas nuevas del `messages.yml` predeterminado con el tuyo

> 💡 **Consejo:** Haz una copia de seguridad de `plugins/BetterDeathMessages/deaths.json` antes de actualizar — contiene las estadísticas de kills/muertes de por vida de cada jugador.

---

## 🗑️ Desinstalación

1. Elimina `BetterDeathMessages-*.jar` de `plugins/`
2. (Opcional) elimina `plugins/BetterDeathMessages/` para borrar todas las configuraciones y estadísticas

> ⚠️ **Advertencia:** Eliminar `deaths.json` es irreversible — no hay manera de reconstruirlo desde los registros del servidor.

---

## 🆘 Solución de Problemas

| Síntoma | Causa Probable | Solución |
|---------|----------------|----------|
| El plugin no carga | Java < 21 | Actualiza el JRE a Java 21+ |
| `NoSuchMethodError: deathMessage` | El servidor es Spigot, no Paper | Cambia a Paper 1.21.x |
| Los mensajes de muerte de vainilla siguen apareciendo | Plugin deshabilitado o se usó `/reload` | Comprueba la consola; usa `/bdm reload`, no `/reload` |
| Los placeholders muestran `%bdm_kills%` literalmente | PAPI no instalado o no recargado | Instala PAPI, ejecuta `/papi reload` |
| Faltan las stats de hover en la transmisión | `global-broadcast.hover-stats: false` | Establece `true` en `messages.yml` |
| La transmisión de primera muerte del día no se dispara | Función deshabilitada | Establece `first-death-of-day.enabled: true` |

---

🏠 [Inicio](Home.md) · 🎮 [Comandos y Permisos](Comandos-y-Permisos.md) · ⚙️ [Configuración](Configuracion.md) · 📊 [PlaceholderAPI](PlaceholderAPI.md)
