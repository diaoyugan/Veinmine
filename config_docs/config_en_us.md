# Vein Mine Configuration Guide

Press `V + M` to open the settings. You can also edit `config/veinmine.json` in your game directory.

## Protected Tools and Chain Ignore List

Search either list by name or ID and tick the entries you want to add. In a world, you can browse the full lists with in-game names and icons where available. Choose **Show selected** to review your selections. You can also enter IDs and edit your selections from the main menu.

Enter a full ID such as `minecraft:diamond_pickaxe` or type `stone` to add `minecraft:stone`. If the game can't find an item or block for an ID, it keeps the entry until you untick it.

Choose **Done** to return to the settings, then choose **Save & Close**.

## Multiplayer

On servers that support syncing, your tool protection settings and ignore list are sent when you join and when you save changes. The server controls search modes, range, and limits.

LAN hosts can change **Server Rules** under **Advanced**. Guests use the host's settings until their own arrive. On a dedicated server, set `serverRuleMode` in `config/veinmine.json`.

| `serverRuleMode` | Which settings apply |
|---|---|
| `CLIENT` (default) | Use the player's settings. Use the server's settings until the player sends their own. |
| `OVERRIDE` | Use the server's settings for everyone. |
| `APPEND` | Combine the ignore lists and apply any tool protection enabled by either side. When both sides protect the same tool, keep the higher minimum durability. |

For example, a server using `APPEND` can require iron pickaxes to keep at least 20 durability. That still applies to a player who turns off their own tool protection. Server rules apply on that server and leave the player's local settings intact.

To add the server's rules to the player's settings, set `serverRuleMode` to `APPEND` in `config/veinmine.json`.

```json
{
  "serverRuleMode": "APPEND"
}
```

Set the tools to protect and the blocks to ignore in the same file. Run `/veinmine reload` to apply your changes.

## General Settings

| Key | Type | Scope | Default | Description |
|---|---|---|---|---|
| `searchRadius` | Integer | Server | `1` | How far range search looks in each direction from the first block. A radius of `1` checks a `3 × 3 × 3` area. |
| `BFSLimit` | Integer | Server | `50` | Maximum number of blocks in a chain search. |
| `ignoredBlocks` | String[] | Player | `[]` | Use range search when mining starts from a listed block. With range search off, no chain starts. |
| `useBFS` | Boolean | Server | `true` | Enable chain search. When off, try range search directly. |
| `useRadiusSearch` | Boolean | Server | `true` | Enable range search. |
| `useRadiusSearchWhenReachBFSLimit` | Boolean | Server | `true` | Try range search when the chain exceeds its limit. Requires range search to be enabled. |
| `highlightBlocksMessage` | Boolean | Client | `true` | Show how many blocks the search found. |

Air, cave air, and void air are always skipped as starting blocks. If normal iron ore and deepslate iron ore are set to match, ignoring one also affects the other. The same applies to dyed blocks set to match across colors.

## Block Matching

| Key | Type | Scope | Default | Description |
|---|---|---|---|---|
| `distinguishCropMaturity` | Boolean | Server | `false` | Match mature and immature crops separately for blocks in the `minecraft:crops` tag. |
| `distinguishDyedBlockColors` | Boolean | Server | `true` | Match dyed blocks by color. Turn this off to let colors in the same family join the same chain. |
| `distinguishDeepslateOres` | Boolean | Server | `false` | Match normal and deepslate variants separately. Turn this off to let both variants of the same ore join the same chain. |
| `bridgeOneBlockGap` | Boolean | Server | `false` | Continue the search past one non-matching block in the same direction. The skipped block stays in place. This setting is under **Advanced**. |

## Tools & Protection

| Key | Type | Scope | Default | Description |
|---|---|---|---|---|
| `protectTools` | Boolean | Player | `true` | Enable tool protection. If durability is too low, the chain stops after the block you broke by hand. |
| `protectedTools` | String[] | Player | `[]` | Tools added by hand, such as `minecraft:iron_pickaxe`. |
| `protectAllDefaultValuableTools` | Boolean | Player | `true` | When tool protection is enabled, automatically protect gold, diamond, and netherite pickaxes, axes, shovels, swords, and hoes. |
| `durabilityThreshold` | Integer | Player | `10` | How much durability a protected tool should have left after vein mining. The settings screen accepts `1` through `256`. |

## Highlights

| Key | Type | Scope | Default | Description |
|---|---|---|---|---|
| `enableHighlights` | Boolean | Client | `true` | Show outlines around the blocks found by the search. |
| `red` | Integer 0–255 | Client | `255` | Red component of the highlight color. |
| `green` | Integer 0–255 | Client | `255` | Green component of the highlight color. |
| `blue` | Integer 0–255 | Client | `255` | Blue component of the highlight color. |
| `alpha` | Integer 0–255 | Client | `255` | Highlight opacity, shown as a percentage in the settings. |

## Keys & Bindings

| Key | Type | Scope | Default | Description |
|---|---|---|---|---|
| `useHoldInsteadOfToggle` | Boolean | Client | `false` | Hold the key to activate vein mining. |
| `configScreenKey` | String[] | Client | `["key.keyboard.v", "key.keyboard.m"]` | Key combination that opens the settings. |

## Advanced

| Key | Type | Scope | Default | Description |
|---|---|---|---|---|
| `useIntrusiveCode` | Boolean | Client | `true` | Show highlight outlines through blocks. If it causes rendering problems, turn it off and restart the game. |

