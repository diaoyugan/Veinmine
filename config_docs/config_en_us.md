# Vein Mine — Configuration Guide

This document explains the configuration options in the mod settings screen and the meanings of fields in the JSON configuration file.

## General Settings
| Key                                | Type     | Scope  | Default | Description                                                                                                                                  |
|------------------------------------|----------|--------|---------|----------------------------------------------------------------------------------------------------------------------------------------------|
| `searchRadius`                     | Integer  | Server | `1`     | Radius used in radius search mode. `1` includes 8 adjacent blocks. Counting two layers in front and back adds 9 each, plus the center block. |
| `BFSLimit`                         | Integer  | Server | `50`    | Maximum number of blocks for BFS (chain search) mode.                                                                                        |
| `ignoredBlocks`                    | String[] | Server | `[]`    | Blocks ignored in chain search (will use radius search instead). Format: `namespace:path` (e.g., `minecraft:air`).                           |
| `useBFS`                           | Boolean  | Server | `true`  | Enable chain (BFS) search mode. If disabled, all searches use radius search.                                                                 |
| `useRadiusSearch`                  | Boolean  | Server | `true`  | Enable radius search. If disabled, radius search will never be used.                                                                         |
| `useRadiusSearchWhenReachBFSLimit` | Boolean  | Server | `true`  | Switch to radius search automatically when the BFS limit is exceeded. If disabled, exceeding the limit will stop chain mining.               |
| `highlightBlocksMessage`           | Boolean  | Server | `true`  | Show the client a message of how many blocks will be broken (e.g., Target blocks: 9).                                                        |

---

## Tools & Protection
| Key                              | Type     | Scope  | Default | Description                                                                                       |
|----------------------------------|----------|--------|---------|---------------------------------------------------------------------------------------------------|
| `protectTools`                   | Boolean  | Server | `true`  | Show a warning and stop chain mining when a tool is about to break.                               |
| `protectedTools`                 | String[] | Server | `[]`    | Tools that will trigger protection. Format: `namespace:path` (e.g., `minecraft:diamond_pickaxe`). |
| `protectAllDefaultValuableTools` | Boolean  | Server | `true`  | Automatically protect common valuable tools (gold, diamond, netherite).                           |
| `durabilityThreshold`            | Integer  | Server | `10`    | Trigger protection if tool durability falls below this after mining.                              |

---

## Highlights
| Key          | Type          | Scope  | Default | Description                                                                       |
|--------------|---------------|--------|---------|-----------------------------------------------------------------------------------|
| `red`        | Integer 0–255 | Client | `255`   | Red channel for highlights.                                                       |
| `green`      | Integer 0–255 | Client | `255`   | Green channel for highlights.                                                     |
| `blue`       | Integer 0–255 | Client | `255`   | Blue channel for highlights.                                                      |
| `alpha`      | Integer 0–255 | Client | `255`   | Highlight opacity (shown as percentage in UI).                                    |
| `renderTime` | Integer 1–3   | Client | `1`     | Number of render passes. Higher values improve visibility but reduce performance. |

---

## Keys & Bindings
| Key                      | Type    | Scope  | Default | Description                                                           |
|--------------------------|---------|--------|---------|-----------------------------------------------------------------------|
| `useHoldInsteadOfToggle` | Boolean | Client | `false` | If enabled, the key must be held to activate. Disabled = toggle mode. |

---

## Advanced Options
| Key                | Type    | Scope  | Default | Description                                                                                                                                                                                                                                |
|--------------------|---------|--------|---------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `useIntrusiveCode` | Boolean | Client | `true`  | Enable custom RenderLayer and RenderPipeline for wall-penetrating highlights. May conflict with certain mods, shaders, or renderers. If crashes or invisible highlights occur, disable this option. Changes require restarting the client. |

