package top.diaoyugan.vein_mine.utils;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.PlayerEntity;

public class Utils {
    public static boolean isToolSuitable(BlockState blockState, PlayerEntity player){
        ItemStack tool = player.getMainHandStack();
        if (blockState.isToolRequired()){
            return tool.isSuitableFor(blockState);
        }
        return true;
    }
}
