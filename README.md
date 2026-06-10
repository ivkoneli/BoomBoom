# BoomBoom

A Minecraft mod for **Minecraft 1.21.11** using **NeoForge 21.11.42**.

## Requirements

- Java 17+ on your PATH (Gradle auto-downloads the JDK 21 toolchain used for compiling)

## Run / Build

On Windows (PowerShell/cmd):

```powershell
# Launch a dev instance of Minecraft with the mod loaded
.\gradlew.bat runClient

# Build the release jar (output: build/libs/boomboom-<version>.jar)
.\gradlew.bat build
```

On Linux/macOS use `./gradlew` instead of `.\gradlew.bat`.

To play with the mod normally, drop the built jar into your `.minecraft/mods` folder and launch the `neoforge-21.11.42` version from your launcher.

The first Gradle run is slow (downloads Gradle, a JDK toolchain, and decompiles Minecraft); subsequent runs are fast.
