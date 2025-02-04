package top.diaoyugan.vein_mine;

import java.util.HashSet;
import java.util.Set;

public class ConfigItems {
    private final int version = 1; // Config file version, modify after config format change, not used now

    public int searchRadius = 1;
    public int BFSLimit = 50;
    public Set<String> ignoredBlocks = new HashSet<>();
    public boolean useBFS = true;
    public boolean useRadiusSearch = true;
    public boolean useRadiusSearchWhenReachBFSLimit = true;
}
