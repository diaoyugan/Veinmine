package top.diaoyugan.vein_mine.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.PlayerEntity;
import java.util.Set;

public class Utils {
    private static final Set<Block> UNIVERSALLY_HARVESTABLE_BLOCKS = Set.of(
            // 潜影盒
            Blocks.SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX,
            Blocks.CYAN_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX,
            Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX,
            Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX,
            Blocks.YELLOW_SHULKER_BOX,

            // 头颅
            Blocks.PLAYER_HEAD, Blocks.CREEPER_HEAD, Blocks.DRAGON_HEAD, Blocks.ZOMBIE_HEAD,
            Blocks.SKELETON_SKULL, Blocks.WITHER_SKELETON_SKULL,

            // 泥土类
            Blocks.DIRT, Blocks.GRASS_BLOCK, Blocks.GRAVEL, Blocks.PODZOL, Blocks.COARSE_DIRT, Blocks.ROOTED_DIRT,
            Blocks.DIRT_PATH, Blocks.MYCELIUM, Blocks.CLAY, Blocks.SAND, Blocks.RED_SAND, Blocks.SOUL_SAND,
            Blocks.SOUL_SOIL, Blocks.FARMLAND,

            // 混凝土粉
            Blocks.WHITE_CONCRETE_POWDER, Blocks.ORANGE_CONCRETE_POWDER, Blocks.MAGENTA_CONCRETE_POWDER,
            Blocks.LIGHT_BLUE_CONCRETE_POWDER, Blocks.YELLOW_CONCRETE_POWDER, Blocks.LIME_CONCRETE_POWDER,
            Blocks.PINK_CONCRETE_POWDER, Blocks.GRAY_CONCRETE_POWDER, Blocks.LIGHT_GRAY_CONCRETE_POWDER,
            Blocks.CYAN_CONCRETE_POWDER, Blocks.PURPLE_CONCRETE_POWDER, Blocks.BLUE_CONCRETE_POWDER,
            Blocks.BROWN_CONCRETE_POWDER, Blocks.GREEN_CONCRETE_POWDER, Blocks.RED_CONCRETE_POWDER,
            Blocks.BLACK_CONCRETE_POWDER,

            // 叶子
            Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES, Blocks.BIRCH_LEAVES, Blocks.JUNGLE_LEAVES,
            Blocks.ACACIA_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.MANGROVE_LEAVES, Blocks.CHERRY_LEAVES,
            Blocks.AZALEA_LEAVES, Blocks.FLOWERING_AZALEA_LEAVES, Blocks.CRIMSON_FUNGUS, Blocks.WARPED_FUNGUS,

            // 蘑菇
            Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM, Blocks.MUSHROOM_STEM, Blocks.CRIMSON_ROOTS,
            Blocks.WARPED_ROOTS, Blocks.NETHER_SPROUTS,

            // 花
            Blocks.POPPY, Blocks.DANDELION, Blocks.BLUE_ORCHID, Blocks.ALLIUM, Blocks.AZURE_BLUET,
            Blocks.RED_TULIP, Blocks.ORANGE_TULIP, Blocks.WHITE_TULIP, Blocks.PINK_TULIP, Blocks.OXEYE_DAISY,
            Blocks.CORNFLOWER, Blocks.LILY_OF_THE_VALLEY, Blocks.WITHER_ROSE, Blocks.SUNFLOWER, Blocks.LILAC,
            Blocks.ROSE_BUSH, Blocks.PEONY, Blocks.LARGE_FERN, Blocks.TALL_GRASS, Blocks.FERN,

            // 木制品
            Blocks.CRAFTING_TABLE, Blocks.BOOKSHELF, Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.BARREL,
            Blocks.COMPOSTER, Blocks.LOOM, Blocks.CARTOGRAPHY_TABLE, Blocks.FLETCHING_TABLE, Blocks.SMITHING_TABLE,

            // 木头 & 木板
            Blocks.OAK_LOG, Blocks.SPRUCE_LOG, Blocks.BIRCH_LOG, Blocks.JUNGLE_LOG, Blocks.ACACIA_LOG,
            Blocks.DARK_OAK_LOG, Blocks.MANGROVE_LOG, Blocks.CHERRY_LOG, Blocks.CRIMSON_STEM, Blocks.WARPED_STEM,
            Blocks.STRIPPED_OAK_LOG, Blocks.STRIPPED_SPRUCE_LOG, Blocks.STRIPPED_BIRCH_LOG, Blocks.STRIPPED_JUNGLE_LOG,
            Blocks.STRIPPED_ACACIA_LOG, Blocks.STRIPPED_DARK_OAK_LOG, Blocks.STRIPPED_MANGROVE_LOG,
            Blocks.STRIPPED_CHERRY_LOG, Blocks.STRIPPED_CRIMSON_STEM, Blocks.STRIPPED_WARPED_STEM,

            Blocks.OAK_PLANKS, Blocks.SPRUCE_PLANKS, Blocks.BIRCH_PLANKS, Blocks.JUNGLE_PLANKS, Blocks.ACACIA_PLANKS,
            Blocks.DARK_OAK_PLANKS, Blocks.MANGROVE_PLANKS, Blocks.CHERRY_PLANKS, Blocks.CRIMSON_PLANKS,
            Blocks.WARPED_PLANKS,

            Blocks.OAK_WOOD, Blocks.SPRUCE_WOOD, Blocks.BIRCH_WOOD, Blocks.JUNGLE_WOOD, Blocks.ACACIA_WOOD,
            Blocks.DARK_OAK_WOOD, Blocks.MANGROVE_WOOD, Blocks.CHERRY_WOOD, Blocks.CRIMSON_HYPHAE, Blocks.WARPED_HYPHAE,
            Blocks.STRIPPED_OAK_WOOD, Blocks.STRIPPED_SPRUCE_WOOD, Blocks.STRIPPED_BIRCH_WOOD, Blocks.STRIPPED_JUNGLE_WOOD,
            Blocks.STRIPPED_ACACIA_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD, Blocks.STRIPPED_MANGROVE_WOOD,
            Blocks.STRIPPED_CHERRY_WOOD, Blocks.STRIPPED_CRIMSON_HYPHAE, Blocks.STRIPPED_WARPED_HYPHAE,

            // 栅栏 & 门
            Blocks.ACACIA_FENCE, Blocks.BIRCH_FENCE, Blocks.DARK_OAK_FENCE, Blocks.JUNGLE_FENCE, Blocks.OAK_FENCE,
            Blocks.SPRUCE_FENCE, Blocks.CRIMSON_FENCE, Blocks.WARPED_FENCE, Blocks.ACACIA_FENCE_GATE, Blocks.BIRCH_FENCE_GATE,
            Blocks.DARK_OAK_FENCE_GATE, Blocks.JUNGLE_FENCE_GATE, Blocks.OAK_FENCE_GATE, Blocks.SPRUCE_FENCE_GATE,
            Blocks.CRIMSON_FENCE_GATE, Blocks.WARPED_FENCE_GATE, Blocks.ACACIA_DOOR, Blocks.BIRCH_DOOR, Blocks.DARK_OAK_DOOR,
            Blocks.JUNGLE_DOOR, Blocks.OAK_DOOR, Blocks.SPRUCE_DOOR, Blocks.CRIMSON_DOOR, Blocks.WARPED_DOOR,

            // 红石
            Blocks.REDSTONE_WIRE, Blocks.REDSTONE_TORCH, Blocks.REDSTONE_BLOCK, Blocks.REDSTONE_LAMP, Blocks.REDSTONE_WALL_TORCH,
            Blocks.REPEATER, Blocks.COMPARATOR, Blocks.CRAFTER, Blocks.OBSERVER, Blocks.PISTON, Blocks.STICKY_PISTON,
            Blocks.PISTON_HEAD, Blocks.MOVING_PISTON, Blocks.CHISELED_BOOKSHELF
    );

    public static boolean isToolSuitable(BlockState blockState, PlayerEntity player) {
        ItemStack tool = player.getMainHandStack();

        // 如果是这些方块，不管工具适不适合，都返回 true
        if (UNIVERSALLY_HARVESTABLE_BLOCKS.contains(blockState.getBlock())) {
            return true;
        }

        return blockState.isToolRequired() && tool.isSuitableFor(blockState);
    }
}
