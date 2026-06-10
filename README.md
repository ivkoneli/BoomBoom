# BoomBoom

A Minecraft mod for **Minecraft 1.21.11** + **NeoForge 21.11.42** that makes (almost) every mob explode like a creeper. Get within 3 blocks of any mob and it primes — hiss, white glow, 1.5 seconds, boom. Break line of sight or run far enough away and it defuses. Creepers are unchanged; they were already perfect.

## Installing the mod (for playing)

1. Have **Minecraft 1.21.11** installed (run it at least once).
2. Install **NeoForge 21.11.42**:
   - Download the installer from the official site: <https://projects.neoforged.net/neoforged/neoforge> (pick version 21.11.42, "Installer"), or use this direct link: <https://maven.neoforged.net/releases/net/neoforged/neoforge/21.11.42/neoforge-21.11.42-installer.jar>
   - Run the installer, choose **Install client**, and point it at your `.minecraft` folder (the default is correct for most people).
3. Download `boomboom-1.0.0.jar` from the [latest release](https://github.com/ivkoneli/BoomBoom/releases/latest) and put it into your mods folder: press `Win+R`, enter `%APPDATA%\.minecraft\mods`, drop the jar there.
4. Launch the game with the **`neoforge-21.11.42`** profile/version selected in your launcher (works with the official launcher and TLauncher alike).

Mob explosions respect the `mobGriefing` gamerule if you want exploding mobs without losing terrain: `/gamerule mobGriefing false`.

## Development

Requires Java 17+ on your PATH (Gradle downloads the JDK 21 toolchain it compiles with).

On Windows (PowerShell/cmd):

```powershell
# Launch a dev instance of Minecraft with the mod loaded
.\gradlew.bat runClient

# Build the release jar (output: build/libs/boomboom-<version>.jar)
.\gradlew.bat build
```

On Linux/macOS use `./gradlew` instead of `.\gradlew.bat`.

The first Gradle run is slow (downloads Gradle, a JDK toolchain, and decompiles Minecraft); subsequent runs are fast.
