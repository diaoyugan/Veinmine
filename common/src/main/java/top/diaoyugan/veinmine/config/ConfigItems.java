package top.diaoyugan.veinmine.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConfigItems { // Default configuration
    public int version = 2; // Config file version, modify after config format change

    public boolean useIntrusiveCode = true;
    public int searchRadius = 1;
    public int BFSLimit = 50;
    public Set<String> ignoredBlocks = new HashSet<>();
    public boolean useBFS = true;
    public boolean useRadiusSearch = true;
    public boolean useRadiusSearchWhenReachBFSLimit = true;
    public boolean distinguishCropMaturity = false;
    public boolean distinguishDyedBlockColors = true;
    public boolean distinguishDeepslateOres = false;
    public boolean bridgeOneBlockGap = false;
    public boolean highlightBlocksMessage = true;
    public boolean protectTools = true;
    public Set<String> protectedTools = new HashSet<>();
    public int durabilityThreshold = 10;
    public boolean protectAllDefaultValuableTools = true;
    public boolean enableHighlights = true;
    public int red = 255;
    public int green = 255;
    public int blue = 255;
    public int alpha = 255;
    public boolean useHoldInsteadOfToggle = false;

    public List<String> configScreenKey = List.of("key.keyboard.v", "key.keyboard.m");
}
