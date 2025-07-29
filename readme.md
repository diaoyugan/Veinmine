

# Vein Mine - Minecraft Mod
English [简体中文](readme_zhcn.md)

## Introduction
Veinmine is a simple, lightweight, and fast-updating Minecraft mod that allows players to efficiently mine connected blocks of the same type. that allows players to efficiently mine connected blocks of the same type. Whether you're mining ores, chopping trees, or gathering resources, this mod makes the process faster and more convenient!

## Features
- **Vein Mining:** Break an entire vein of ores or a group of connected blocks with a single action.
- **Configurable Settings:** Adjust the mining range, block types, and activation methods through an intuitive settings menu.
- **Keybind Support:** Toggle vein mining on/off with a customizable key.
- **Blacklist:** Control which blocks can't be vein-mined.
- **Compatibility:** Works seamlessly with other mods and custom blocks.

## Installation
### Requirements
- Minecraft
- Fabric API ([Download](https://modrinth.com/mod/fabric-api))
- Fabric Loader ([Download](https://fabricmc.net/use/))

## Recommends
It's recommended to install [**ModMenu**](https://modrinth.com/mod/modmenu) and [**Cloth Config API**](https://www.curseforge.com/minecraft/mc-mods/cloth-config) for better mod management and easier configuration.

### Steps
1. Install [Fabric Loader](https://fabricmc.net/use/).
2. Download and install [Fabric API](https://modrinth.com/mod/fabric-api).
3. Download the latest version that's compatible with your game version of Vein Mine.
4. Place the mod file into the `mods` folder of your Minecraft directory.
5. Launch the game and enjoy!

## Configuration
- Open the **modmenu** to customize Vein Mine's behavior. OR
- Edit the configuration file located in `.minecraft/config/veinmine.json`.
- For servers, the configuration file located in `[your Minecraft server directory]/config/veinmine.json`
## Controls
- **Press `(~) (default)** to toggle Vein Mine.

## Support & Feedback
If you encounter any issues or have any suggestions, feel free to open an issue on our [GitHub Issues](https://github.com/diaoyugan/Veinmine/issues) page.
Or, if you're interested in contribution to our code, welcome to open a PR on GitHub!

## Build
For 1.21.3 to 1.21.4 run

`./gradlew build -PmcVersion v1_21_3_4`

For 1.21.5 run

`./gradlew build -PmcVersion v1_21_5`

For 1.21.6 to 1.21.8 run

`./gradlew build -PmcVersion v1_21_6_8`

## License
This mod is licensed under the GNU General Public License Version 3. See [LICENSE](LICENSE) for details.
