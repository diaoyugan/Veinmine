# Build Guide

This branch targets **Minecraft 1.20.1 / 1.20.4 / 1.20.6** via Fabric. Pick the target MC version through the `mc_version` Gradle property.

## Requirements

| Target MC | JDK | Gradle (wrapper) | Loom |
|-----------|-----|------------------|------|
| 1.20.1    | 17  | 8.8              | 1.7.4 |
| 1.20.4    | 17  | 8.8              | 1.7.4 |
| 1.20.6    | 21  | 8.8              | 1.7.4 |

Loom enforces that the **Gradle daemon itself** runs on the JDK the target MC version expects — not just the compile-time toolchain. Building 1.20.6 while Gradle runs on Java 17 fails with:

```
Minecraft 1.20.6 requires Java 21 but Gradle is using 17
```

Switch `JAVA_HOME` before invoking `./gradlew` whenever you change targets between the 17-group and the 21-group.

### Installing JDKs (macOS / Homebrew)

```bash
brew install openjdk@17 openjdk@21
```

Canonical paths after install:

```
/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home
```

Linux / Windows: use `sdkman`, `asdf`, your distro packages, or the installers from adoptium.net — just point `JAVA_HOME` at the JDK home directory.

## Building a single version

```bash
# 1.20.1 (Java 17)
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
./gradlew build -Pmc_version=v1_20_1

# 1.20.4 (Java 17)
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
./gradlew build -Pmc_version=v1_20_4

# 1.20.6 (Java 21)
export JAVA_HOME=/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home
./gradlew build -Pmc_version=v1_20_6
```

The default `mc_version` in `gradle.properties` is `v1_20_1`, so `./gradlew build` (no `-P`) builds 1.20.1.

Output jars land in `build/libs/`:

```
veinmine-<mod_version>+1.20.1.jar
veinmine-<mod_version>+1.20.4.jar
veinmine-<mod_version>+1.20.6.jar
```

plus the corresponding `-sources.jar`.

## Development workflow

Run a dev client (uses the live source set — edit, rerun, test):

```bash
./gradlew runClient -Pmc_version=v1_20_1
```

Run a dev integrated server:

```bash
./gradlew runServer -Pmc_version=v1_20_1
```

Both commands honor the same `JAVA_HOME` rule as `build`.

## Clean build

If you just switched `mc_version` and Loom's remap cache gets confused:

```bash
./gradlew clean
./gradlew build -Pmc_version=v1_20_X
```

## Building all three versions in one shot

`./gradlew build` overwrites `build/libs/` each run, so copy the jars aside between runs if you want all three:

```bash
JDK17=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
JDK21=/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home

mkdir -p dist

JAVA_HOME=$JDK17 ./gradlew clean build -Pmc_version=v1_20_1 && cp build/libs/veinmine-*+1.20.1.jar dist/
JAVA_HOME=$JDK17 ./gradlew clean build -Pmc_version=v1_20_4 && cp build/libs/veinmine-*+1.20.4.jar dist/
JAVA_HOME=$JDK21 ./gradlew clean build -Pmc_version=v1_20_6 && cp build/libs/veinmine-*+1.20.6.jar dist/

ls dist/
```

## Project layout

- `src/main/java/` — version-agnostic common code (logic, `ServerNetBridge` / `ClientNetBridge` interfaces, `NetHandlers`, config, utils)
- `src/main/v1_20_1/java/` — 1.20.1 overrides: legacy `PacketByteBuf` networking, `ItemStack.damage(int, LivingEntity, Consumer)` overload
- `src/main/v1_20_4/java/` — 1.20.4 overrides: same API shape as 1.20.1
- `src/main/v1_20_6/java/` — 1.20.6 overrides: new `PayloadTypeRegistry` / `CustomPayload` API, `ItemStack.damage(int, LivingEntity, EquipmentSlot)` overload, `Payloads.java` record wrappers
- `src/main/resources/mixins.v1_20_{1,4,6}.json` — per-version mixin configs (currently empty — no mixins needed)

`build.gradle` overlays `src/main/${mc_version}/java` on top of `src/main/java` at configure time based on the `mc_version` property.

## Version maps (`build.gradle`)

Changing the Yarn / Fabric API / ModMenu / Cloth Config / Loader versions pulled for each target happens in the maps near the top of `build.gradle`:

- `yarnMappings`
- `fabricApiVersions`
- `modMenuVersions`
- `clothConfigVersions`
- `loaderVersion`
- `javaVersions` (per-target JDK major version)

Keep the keys (`v1_20_1` / `v1_20_4` / `v1_20_6`) in sync across all six maps.

## Troubleshooting

- **`JvmVendorSpec ... IBM_SEMERU` error** — you're running Gradle 9.x. This branch needs 8.x. The wrapper is pinned to 8.8; make sure you're invoking `./gradlew`, not a system-wide `gradle`.
- **`Minecraft 1.20.6 requires Java 21 but Gradle is using 17`** — switch `JAVA_HOME` to Java 21 before running the build.
- **`Could not find eu.pb4:placeholder-api...`** — this is a transitive dep of ModMenu 9.2.0. The `maven.nucleoid.xyz` entry in `build.gradle`'s `repositories` block provides it. If the repo is down, temporarily downgrade `modMenuVersions["v1_20_4"]` to a build that doesn't depend on placeholder-api.
- **`permission denied: ./gradlew`** — `chmod +x gradlew` once.
- **First-run slowness** — Loom downloads vanilla Minecraft, mappings, and Fabric API on first configure per target. 1–2 minutes is normal. Subsequent builds are cached and take a few seconds.
