package top.diaoyugan.veinmine.client.render;

import net.minecraft.client.Camera;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class OutlineBuilder {
    private OutlineBuilder() {}

    private static final int[][] EDGES = {
            {0,1},{1,2},{2,3},{3,0},
            {4,5},{5,6},{6,7},{7,4},
            {0,4},{1,5},{2,6},{3,7},
    };

    public enum LineStyle {
        THIN_LINES,
        RIBBON_THICK_LINES,
        OLD_TRIANGLES
    }

    public record Color(float r, float g, float b, float a) {}

    public record Line(Vec3 from, Vec3 to, Color color) {}

    public static List<Line> buildLines(
            Camera camera,
            Collection<BlockPos> blocks,
            Color color
    ) {
        List<Line> result = new ArrayList<>();
        if (blocks.isEmpty()) return result;

        Vec3 camPos = camera.position();

        for (BlockPos pos : blocks) {
            double x = pos.getX() - camPos.x;
            double y = pos.getY() - camPos.y;
            double z = pos.getZ() - camPos.z;

            Vec3[] c = {
                    new Vec3(x, y, z),
                    new Vec3(x + 1, y, z),
                    new Vec3(x + 1, y + 1, z),
                    new Vec3(x, y + 1, z),
                    new Vec3(x, y, z + 1),
                    new Vec3(x + 1, y, z + 1),
                    new Vec3(x + 1, y + 1, z + 1),
                    new Vec3(x, y + 1, z + 1)
            };

            for (int[] e : EDGES) {
                result.add(new Line(c[e[0]], c[e[1]], color));
            }
        }

        return result;
    }
}