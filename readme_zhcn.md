# Vein Mine - Minecraft 模组

[English](readme.md) 简体中文

## 简介
连锁挖掘是一个简洁，轻量化，多加载器，更新快速的 Minecraft 模组，允许玩家高效地挖掘相连的相同类型方块。无论是挖矿、砍树还是收集资源，该模组都能让过程更加快捷便利

## 特性
- **连锁破坏**：一次性破坏整个矿脉或一组相连方块。
- **可配置设置**：可通过直观的设置菜单调整挖矿范围、方块类型和激活方式。
- **按键绑定**：使用可自定义的快捷键启用或禁用连锁挖矿。
- **黑名单**：控制哪些方块可以被连锁挖掘。
- **兼容性**：与其他模组及自定义方块兼容。

### 推荐安装
对于Fabric 推荐安装 [**ModMenu**](https://modrinth.com/mod/modmenu) 和 [**Cloth Config API**](https://www.curseforge.com/minecraft/mc-mods/cloth-config) 以便更好地管理和配置模组。

## 配置
- 打开 **模组**页面 进行配置。
- 或者可以编辑 `.minecraft/config/veinmine.json`进行设置。
- 对于服务端 可以编辑 `[服务端根目录]/config/veinmine.json`
- [配置文件说明](config_docs/config_zh_cn.md)

## 控制
- **按下 `(~)（默认）** 切换激活连锁挖掘。

## 支持与反馈
如果遇到问题或有建议，请在 [GitHub Issues](https://github.com/diaoyugan/Veinmine/issues) 页面提交问题。
如果你想协助我们改进代码，欢迎PR

## 构建
此分支为Minecraft 26版本设计 1.21请前往1.21-legacy分支

26.1 运行

`./gradlew build -PmcVersion v26_1`

## 许可证
本模组基于 GNU GPLv3 许可证发布。详见 [LICENSE](LICENSE)。
