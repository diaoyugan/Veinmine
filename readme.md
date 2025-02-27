# Vein Mine - Minecraft Mod
English [简体中文](readme_zhcn.md)

## Introduction
Vein Mine is a Minecraft mod that allows players to efficiently mine connected blocks of the same type. Whether you're mining ores, chopping trees, or gathering resources, this mod makes the process faster and more convenient!

## Features
- **Vein Mining:** Break an entire vein of ores or a group of connected blocks with a single action.
- **Configurable Settings:** Adjust the mining range, block types, and activation methods through an intuitive settings menu.
- **Keybind Support:** Toggle vein mining on/off with a customizable key.
- **Blacklist:** Control which blocks can be vein-mined.
- **Compatibility:** Works seamlessly with other mods and custom blocks.

## Installation
### Requirements
- Minecraft **1.21.4**
- Fabric API **0.115.0** ([Download](https://modrinth.com/mod/fabric-api))
- Fabric Loader ([Download](https://fabricmc.net/use/))

## Recommends
Recommend installing [**ModMenu**](https://modrinth.com/mod/modmenu) and [**Cloth Config API**](https://www.curseforge.com/minecraft/mc-mods/cloth-config) for better mod management and configuration.

### Steps
1. Install [Fabric Loader](https://fabricmc.net/use/).
2. Download and install [Fabric API](https://modrinth.com/mod/fabric-api).
3. Download the latest version of Vein Mine from [GitHub Release](https://github.com/diaoyugan/Veinmine/releases).
4. Place the mod file into the `mods` folder of your Minecraft directory.
5. Launch the game and enjoy!

## Configuration
- Open the **modmenu** to customize Vein Mine's behavior. OR
- Edit the configuration file located in `.minecraft/config/veinmine.json`.
## Controls
- **Press `(~) (default)** to activate Vein Mine.
- Change the activation keybind in the in-game controls settings.

## Support & Feedback
If you encounter any issues or have suggestions, feel free to open an issue on our [GitHub Issues](https://github.com/diaoyugan/Veinmine/issues) page.

## License
This mod is licensed under the GNU GPLv3 License. See [LICENSE](LICENSE) for details.

## TODO:
* [ ] Add a setting to determine whether different-colored shulker boxes (or other colored blocks) should be considered the same type.
* [X] Exclude certain blocks from vein mining, preferably configurable by the user.
* [X] Implement tool durability protection: prevent vein mining when the tool's durability is insufficient to sustain the mining damage (configurable by the user).
* [X] Support Mod Menu and provide a mod settings page with saving functionality.
* [X] Fix an issue where shulker boxes, mob heads (including player heads), etc. (needs more testing) were considered to have an inappropriate mining tool. They should always be considered as having an appropriate tool and be excluded from silk touch logic (to prevent NBT data loss).
* [X] Ensure that blocks that normally do not drop in vanilla but drop with silk touch are included in the silk touch vein mining list.
* [X] Ensure that blocks that never drop in vanilla, even with the correct tool (e.g., spawners), should never drop.
* [X] Fix an issue where blocks that can be broken with any tool (including bare hands) were always considered as having an inappropriate tool for vein mining.
* [X] Implement a `shouldDrop` method to determine whether a block should drop items under any condition.
* [X] Refactor the project structure as the current code placement is not well-organized.
* [X] Complete all TODOs in the code.
* [X] Highlight blocks that will be vein-mined when the activation key is pressed, as well as the block the player is targeting.
* [X] **Important!!! Optimize the vein mining algorithm. The current implementation uses brute force block searching within a range. Improve the algorithm to intelligently determine the vein-mining area (e.g., ore veins) and optimize the search method. Create a new class for this algorithm, as multiple methods will need to use it!**

