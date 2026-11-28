# Vein Mine - Minecraft Mod
English | [简体中文](https://github.com/diaoyugan/Veinmine/blob/master/readme_zhcn.md)
## Introduction

With vein mining enabled, breaking one block can also collect nearby blocks that match it. Use it to mine ores, chop trees, or harvest crops, and choose which blocks count as a match.

## Features

### Vein Mining

* Mine connected blocks with a single action.
* Works with vanilla and modded blocks.
* Configurable activation mode (Toggle or Hold).

### Advanced Block Matching (1.4.1+)

* Distinguish mature and immature crops.
* Distinguish dyed block color variants.
* Distinguish deepslate ores and normal ores.
* Optional one-block gap bridging.

### Tool Protection

* Choose which tools to protect and how much durability they should keep. If the chain would leave less than that, you only break the first block.
* Search for tools and blocks to add to your lists, or enter their IDs directly.

### Multiplayer

* On servers that support syncing, players can choose which tools to protect and which blocks to skip in chain search.
* Server owners can use players' settings, add their own rules, or apply the same rules to everyone.

### Highlighting

* See which blocks the search finds before you mine. The chain may still stop if your tool is low on durability.

## Configuration

Press `V + M` to open the settings with the default keybind.

### Manual Configuration

* Edit `config/veinmine.json` in the relevant game or server directory.
* LAN hosts can change **Server Rules** under **Advanced**. On a dedicated server, edit `serverRuleMode` in `config/veinmine.json` and run `/veinmine reload`.
* See the [Configuration Guide](config_docs/config_en_us.md) for instructions and default values.

### Automated Tests

Run the command below from the repository root to test singleplayer, LAN, and dedicated-server play. Pass the path to an existing `eula.txt` with `eula=true`. The example path is relative to the test module.

```
.\gradlew.bat --configure-on-demand :fabric:check :fabric:runClientGameTest '-PveinmineTestEulaFile=runs/server/eula.txt'
```

The tests connect on the same machine with offline profiles and a separate settings folder for each guest. Find the test worlds and logs under `fabric/build/run/clientGameTest`.

## Controls

* ~ (default): Toggle Vein Mine
* V + M (default): Open Configuration Screen (1.4.1+)

## Notes

Feature availability may vary between older releases and the latest version.

## Support & Feedback
If you encounter any issues or have any suggestions, feel free to open an issue on our [GitHub Issues](https://github.com/diaoyugan/Veinmine/issues) page.
Or, if you'd like to contribute to the project, welcome to open a PR on GitHub!

## License
This mod is licensed under the GNU General Public License Version 3. See [LICENSE](https://raw.githubusercontent.com/diaoyugan/Veinmine/refs/heads/master/LICENSE) for details.
