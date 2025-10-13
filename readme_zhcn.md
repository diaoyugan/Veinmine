# Vein Mine - Minecraft 模组

[English](readme.md) 简体中文

## 简介
连锁挖掘是一个简洁，轻量化，更新快速的 Minecraft 模组，允许玩家高效地挖掘相连的相同类型方块。无论是挖矿、砍树还是收集资源，该模组都能让过程更加快捷便利

## 特性
- **连锁破坏**：一次性破坏整个矿脉或一组相连方块。
- **可配置设置**：可通过直观的设置菜单调整挖矿范围、方块类型和激活方式。
- **按键绑定**：使用可自定义的快捷键启用或禁用连锁挖矿。
- **黑名单**：控制哪些方块可以被连锁挖掘。
- **兼容性**：与其他模组及自定义方块兼容。

## 安装
### 需求
- Minecraft
- Fabric API ([下载](https://modrinth.com/mod/fabric-api))
- Fabric Loader ([下载](https://fabricmc.net/use/))
## 推荐安装
推荐安装 [**ModMenu**](https://modrinth.com/mod/modmenu) 和 [**Cloth Config API**](https://www.curseforge.com/minecraft/mc-mods/cloth-config) 以便更好地管理和配置模组。

### 步骤
1. 安装 [Fabric Loader](https://fabricmc.net/use/)。
2. 下载并安装 [Fabric API](https://modrinth.com/mod/fabric-api)。
3. 下载 Vein Mine 的最新版本。
4. 将模组文件放入 Minecraft 目录下的 `mods` 文件夹。
5. 启动游戏并享受！

## 配置
- 打开 **modmenu** 自定义 Vein Mine 的行为。
- 或者可以编辑 `.minecraft/config/veinmine.json`进行设置。
- 对于服务端 可以编辑 `[服务端根目录]/config/veinmine.json`
- [配置文件说明](config_docs/config_zh_cn.md)

## 控制
- **按下 `(~)（默认）** 切换激活连锁挖掘。

## 支持与反馈
如果遇到问题或有建议，请在 [GitHub Issues](https://github.com/diaoyugan/Veinmine/issues) 页面提交问题。
如果你想协助我们改进代码，欢迎PR

## 构建
1.21.3到1.21.4 运行

`./gradlew build -PmcVersion v1_21_3_4`

1.21.5 运行

`./gradlew build -PmcVersion v1_21_5`

1.21.6到1.21.8 运行

`./gradlew build -PmcVersion v1_21_6_8`

## 许可证
本模组基于 GNU GPLv3 许可证发布。详见 [LICENSE](LICENSE)。
