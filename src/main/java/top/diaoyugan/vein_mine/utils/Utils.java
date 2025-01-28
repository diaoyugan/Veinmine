package top.diaoyugan.vein_mine.utils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class Utils {
    public static boolean isToolSuitable(BlockState blockState, PlayerEntity player){
        ItemStack tool = player.getMainHandStack();
        return blockState.isToolRequired() && tool.isSuitableFor(blockState);
    }
}