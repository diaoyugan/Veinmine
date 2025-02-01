package top.diaoyugan.vein_mine;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import static top.diaoyugan.vein_mine.vein_mine.ID;

public class InvisibleBlock {
    public static Block register(Block block, RegistryKey<Block> blockKey, boolean shouldRegisterItem) {
        // Sometimes, you may not want to register an item for the block.
        // Eg: if it's a technical block like `minecraft:air` or `minecraft:end_gateway`
        if (shouldRegisterItem) {
            // Items need to be registered with a different type of registry key, but the ID
            // can be the same.
            RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, blockKey.getValue());

            BlockItem blockItem = new BlockItem(block, new Item.Settings().registryKey(itemKey));
            Registry.register(Registries.ITEM, itemKey, blockItem);
        }

        return Registry.register(Registries.BLOCK, blockKey, block);
    }
    public static void initialize() {}

    public static final RegistryKey<Block> INVISIBLE_BLOCK_KEY = RegistryKey.of(
            RegistryKeys.BLOCK,
            Identifier.of(ID, "invisible_block")
    );

    public static final Block CONDENSED_DIRT = register(
            new Block(AbstractBlock.Settings.create().registryKey(INVISIBLE_BLOCK_KEY).sounds(BlockSoundGroup.GRASS)),
            INVISIBLE_BLOCK_KEY,
            false
    );
}

