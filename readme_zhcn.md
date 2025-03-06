# Vein Mine - Minecraft 模组

[English Version](readme.md) 简体中文

## 简介
连锁挖掘是一个 Minecraft 模组，允许玩家高效地挖掘相连的相同类型方块。无论是挖矿、砍树还是收集资源，该模组都能让过程更加快捷便利！

## 特性
- **连锁破坏**：一次性破坏整个矿脉或一组相连方块。
- **可配置设置**：可通过直观的设置菜单调整挖矿范围、方块类型和激活方式。
- **按键绑定**：使用可自定义的快捷键启用或禁用连锁挖矿。
- **黑名单**：控制哪些方块可以被连锁挖掘。
- **兼容性**：与其他模组及自定义方块兼容。

## 安装
### 需求
- Minecraft **1.21.4**
- Fabric API **0.115.0** ([下载](https://modrinth.com/mod/fabric-api))
- Fabric Loader ([下载](https://fabricmc.net/use/))
## 推荐安装
推荐安装 [**ModMenu**](https://modrinth.com/mod/modmenu) 和 [**Cloth Config API**](https://www.curseforge.com/minecraft/mc-mods/cloth-config) 以便更好地管理和配置模组。

### 步骤
1. 安装 [Fabric Loader](https://fabricmc.net/use/)。
2. 下载并安装 [Fabric API](https://modrinth.com/mod/fabric-api)。
3. 从 [GitHub Release](https://github.com/diaoyugan/Veinmine/releases) 下载 Vein Mine 的最新版本。
4. 将模组文件放入 Minecraft 目录下的 `mods` 文件夹。
5. 启动游戏并享受！

## 配置
- 打开 **modmenu** 自定义 Vein Mine 的行为。
- 或者可以编辑 `.minecraft/config/veinmine.json`进行设置。

## 控制
- **按下 `(~)（默认）** 切换激活连锁挖掘。
- 在游戏内的控制设置中修改快捷键。

## 支持与反馈
如果遇到问题或有建议，请在 [GitHub Issues](https://github.com/diaoyugan/Veinmine/issues) 页面提交问题。

## 许可证
本模组基于 GNU GPLv3 许可证发布。详见 [LICENSE](LICENSE)。

## TODO:
* [ ] 添加一个设置，是否将不同颜色的潜影盒（或其他有颜色区分的方块）视为同一类。
* [X] 排除某些方块不参与连锁挖掘，并允许用户自定义配置。
* [X] 添加工具耐久保护：当工具耐久不足以承担连锁破坏时，取消连锁挖掘（用户可自行设置是否开启）。
* [X] 支持 Mod Menu，并提供模组设置页面及配置保存功能。
* [X] 修复潜影盒、生物头颅（包括玩家头颅）等（需要更多测试）被破坏时，任何工具都被视为不合适采集工具的问题，应将它们视为合适工具，并排除在精准采集判定之外（避免 NBT 数据丢失）。
* [X] 确保原版中不会掉落但可以通过精准采集获取的方块，仍可被连锁挖掘。
* [X] 确保原版任何情况下都不会掉落的方块（如刷怪笼），在任何情况下都不应掉落。
* [X] 修复所有工具（包括空手）均无法正确判定连锁采集工具的问题。
* [X] 编写 `shouldDrop` 方法，以判定某方块在特定情况下是否应该掉落物品。
* [X] 重构项目结构，优化代码存放位置，使其更加规范。
* [X] 完成代码中所有 TODO 标记的任务。
* [X] 在快捷键激活时，高亮显示即将被连锁挖掘的方块及玩家当前对准的方块。
* [X] **重要！！！优化连锁算法。目前的实现是对范围内的方块进行暴力搜索，应改进为智能识别连锁区域（如矿脉）并优化搜索方式。需创建一个新类存放此算法，并确保多个方法可以调用！**
