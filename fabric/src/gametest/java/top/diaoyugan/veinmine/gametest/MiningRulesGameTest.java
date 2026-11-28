package top.diaoyugan.veinmine.gametest;

import com.mojang.blaze3d.platform.InputConstants;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestServerContext;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.InputType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.UserWhiteListEntry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import top.diaoyugan.veinmine.client.configScreen.ConfigScreen;
import top.diaoyugan.veinmine.config.Config;
import top.diaoyugan.veinmine.config.ConfigItems.ServerRuleMode;
import top.diaoyugan.veinmine.config.ServerMiningRules;
import top.diaoyugan.veinmine.events.BlockBreak;
import top.diaoyugan.veinmine.networking.MiningPreferencesPacket;
import top.diaoyugan.veinmine.utils.SmartVein;
import top.diaoyugan.veinmine.utils.Utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Properties;
import java.util.Set;
import java.util.function.BooleanSupplier;

public final class MiningRulesGameTest implements FabricClientGameTest {
    private static final String HOST = "VeinMineHost";
    private static final String GUEST = "VeinMineGuest";
    private static final String IRON_PICKAXE = "minecraft:iron_pickaxe";
    private static final String STONE = "minecraft:stone";
    private static final String DEEPSLATE = "minecraft:deepslate";
    private static final Path OLD_POLICY_FILE = Path.of("config", "veinmine-server-rules.json");
    private static final Path CONFIG_FILE = Path.of("config", "veinmine.json");
    private static final String GUEST_PORT = "veinmine.gametest.guestPort";
    private static final Duration CONNECTION_TIMEOUT = Duration.ofSeconds(120);

    @Override
    public void runTest(ClientGameTestContext context) {
        try {
            String port = System.getProperty(GUEST_PORT);
            if (port != null) {
                runGuest(context, Integer.parseInt(port));
                return;
            }

            testSingleplayer(context);
            testLan(context);
            testDedicatedServer(context);
        } catch (IOException error) {
            throw new AssertionError("Unable to run the Vein Mine connection tests", error);
        }
    }

    private static void testSingleplayer(ClientGameTestContext context) throws IOException {
        savePreferences(true, 24, STONE);
        JsonObject oldConfig = readConfig();
        oldConfig.addProperty("version", 2);
        oldConfig.remove("serverRuleMode");
        Files.writeString(CONFIG_FILE, oldConfig.toString());
        Config.getInstance().load();
        check(readConfig().get("serverRuleMode").getAsString().equals("CLIENT"),
                "old configurations gain the default server rule mode on load");

        try (var world = context.worldBuilder().create()) {
            var server = world.getServer();
            check(!server.computeOnServer(MinecraftServer::isPublished), "singleplayer server is private");
            await(context, "singleplayer initial preference packet", () -> server.computeOnServer(mc -> {
                ServerPlayer player = mc.getPlayerList().getPlayerByName(HOST);
                if (player == null) return false;
                var rules = ServerMiningRules.forPlayer(player);
                return rules.primary() != Config.getInstance().miningRules()
                        && rules.requiredDurability(IRON_PICKAXE) == 24 && rules.ignoresBlock(STONE);
            }));
            checkMining(server, HOST, Blocks.STONE, 32, false);
            checkMining(server, HOST, Blocks.COBBLESTONE, 12, false);
            checkMining(server, HOST, Blocks.COBBLESTONE, 25, false, true);
            checkMining(server, HOST, Blocks.COBBLESTONE, 26, true, true);
            checkMining(server, HOST, Blocks.COBBLESTONE, 32, false, true, Integer.MAX_VALUE);
            checkAir(server, HOST);

            context.runOnClient(client -> savePreferences(false, 24, STONE));
            awaitRules(context, server, HOST, -1, true, false);
            checkMining(server, HOST, Blocks.COBBLESTONE, 12, true);
        }
        check(Files.notExists(OLD_POLICY_FILE), "singleplayer uses a single configuration file");
        System.out.println("Vein Mine singleplayer passed");
    }

    private static void testLan(ClientGameTestContext context) throws IOException {
        savePreferences(true, 40, DEEPSLATE);
        try (var world = context.worldBuilder().create()) {
            var server = world.getServer();
            int port = freeLoopbackPort();
            server.runOnServer(mc -> {
                check(mc.publishServer(MinecraftServer.MultiplayerScope.LAN, false, port),
                        "open the integrated server to LAN");
                // The test launcher uses offline profiles. The test mixin binds the listener to loopback.
                mc.setUsesAuthentication(false);
            });
            try (GuestProcess guest = GuestProcess.start(port)) {
                awaitGuestRules(context, server, guest, -1, true, false);
                awaitRules(context, server, HOST, 40, false, true);
                checkMining(server, GUEST, Blocks.STONE, 32, false);
                checkMining(server, GUEST, Blocks.COBBLESTONE, 12, true);
                checkMining(server, HOST, Blocks.COBBLESTONE, 12, false);

                guest.requestUpdate();
                awaitGuestRules(context, server, guest, 24, true, false);
                checkMining(server, GUEST, Blocks.COBBLESTONE, 12, false);

                chooseNextServerRuleMode(context, ServerRuleMode.CLIENT, ServerRuleMode.APPEND);
                awaitGuestRules(context, server, guest, 40, true, true);
                checkMining(server, GUEST, Blocks.COBBLESTONE, 32, false);

                chooseNextServerRuleMode(context, ServerRuleMode.APPEND, ServerRuleMode.OVERRIDE);
                awaitGuestRules(context, server, guest, 40, false, true);
                checkMining(server, GUEST, Blocks.STONE, 80, true);

                guest.finish(context);
                await(context, "LAN guest disconnect", () -> server.computeOnServer(mc ->
                        mc.getPlayerList().getPlayerByName(GUEST) == null));
            }
        }
        check(Files.notExists(OLD_POLICY_FILE), "LAN hosting uses a single configuration file");
        System.out.println("Vein Mine LAN host and guest passed");
    }

    private static void testDedicatedServer(ClientGameTestContext context) throws IOException {
        savePreferences(true, 40, DEEPSLATE);
        int port = freeLoopbackPort();
        Properties properties = new Properties();
        properties.setProperty("level-name", "veinmine-gametest-world");
        properties.setProperty("server-ip", "127.0.0.1");
        properties.setProperty("server-port", Integer.toString(port));
        properties.setProperty("view-distance", "4");
        properties.setProperty("simulation-distance", "4");
        try (var server = context.worldBuilder().createServer(properties)) {
            check(server.computeOnServer(MinecraftServer::isDedicatedServer), "dedicated server topology");
            check(readConfig().get("serverRuleMode").getAsString().equals("CLIENT"), "dedicated default is CLIENT");
            check(Files.notExists(OLD_POLICY_FILE), "dedicated server uses a single configuration file");
            server.runOnServer(mc -> mc.getPlayerList().getWhiteList()
                    .add(new UserWhiteListEntry(NameAndId.createOffline(GUEST))));
            try (GuestProcess guest = GuestProcess.start(port)) {
                awaitGuestRules(context, server, guest, -1, true, false);
                checkMining(server, GUEST, Blocks.COBBLESTONE, 12, true);
                guest.requestUpdate();
                awaitGuestRules(context, server, guest, 24, true, false);
                checkMining(server, GUEST, Blocks.COBBLESTONE, 32, true);

                reloadServerRuleMode(server, ServerRuleMode.APPEND);
                awaitGuestRules(context, server, guest, 40, true, true);
                checkMining(server, GUEST, Blocks.COBBLESTONE, 32, false);
                checkMining(server, GUEST, Blocks.STONE, 80, false);

                reloadServerRuleMode(server, ServerRuleMode.OVERRIDE);
                awaitGuestRules(context, server, guest, 40, false, true);
                checkMining(server, GUEST, Blocks.STONE, 80, true);
                checkMining(server, GUEST, Blocks.DEEPSLATE, 80, false);
                checkAir(server, GUEST);

                guest.finish(context);
                await(context, "dedicated guest disconnect", () -> server.computeOnServer(mc ->
                        mc.getPlayerList().getPlayerByName(GUEST) == null));
            }
        }
        context.setScreen(TitleScreen::new);
        System.out.println("Vein Mine dedicated server passed");
    }

    private static void runGuest(ClientGameTestContext context, int port) throws IOException {
        savePreferences(false, 24, STONE);
        context.runOnClient(client -> {
            String target = "127.0.0.1:" + port;
            var server = new ServerData("Vein Mine test server", target, ServerData.Type.OTHER);
            ConnectScreen.startConnecting(client.gui.screen(), client, ServerAddress.parseString(target), server, false, null);
        });
        await(context, "guest enters world and negotiates preferences channel", () -> context.computeOnClient(client -> {
            if (client.gui.screen() instanceof DisconnectedScreen disconnected) {
                throw new AssertionError("Guest disconnected while logging in: " + disconnected.getTitle().getString());
            }
            return client.player != null && ClientPlayNetworking.canSend(MiningPreferencesPacket.ID);
        }));
        openAdvancedSettings(context);
        context.runOnClient(client -> check(!serverRulesButton(client).active,
                "remote guests cannot change the server rule mode"));
        context.setScreen(() -> null);
        await(context, "guest preference update request", () -> Files.exists(GuestProcess.UPDATE));
        context.runOnClient(client -> savePreferences(true, 24, STONE));
        String preferencesAfterSave = Files.readString(CONFIG_FILE);
        await(context, "guest test completion", () -> Files.exists(GuestProcess.FINISH));
        check(Files.notExists(OLD_POLICY_FILE), "guest uses a single configuration file");
        check(preferencesAfterSave.equals(Files.readString(CONFIG_FILE)), "server rules did not rewrite guest preferences");
        context.runOnClient(client -> {
            client.level.disconnect(Component.literal("Vein Mine test complete"));
            client.disconnectWithSavingScreen();
        });
        context.waitFor(client -> client.level == null);
        context.setScreen(TitleScreen::new);
    }

    private static void savePreferences(boolean protect, int threshold, String ignoredBlock) {
        Config config = Config.getInstance();
        var values = config.getConfigItems();
        values.protectTools = protect;
        values.serverRuleMode = ServerRuleMode.CLIENT;
        values.protectAllDefaultValuableTools = false;
        values.durabilityThreshold = threshold;
        values.protectedTools.clear();
        values.protectedTools.add(IRON_PICKAXE);
        values.ignoredBlocks.clear();
        values.ignoredBlocks.add(ignoredBlock);
        values.useBFS = true;
        values.useRadiusSearch = false;
        values.useRadiusSearchWhenReachBFSLimit = false;
        values.bridgeOneBlockGap = false;
        config.save();
    }

    private static void awaitRules(ClientGameTestContext context, TestServerContext server,
                                   String name, int threshold, boolean stone, boolean deepslate) {
        await(context, name + " rules [threshold=" + threshold + ", stone=" + stone + ", deepslate=" + deepslate + "]",
                () -> hasRules(server, name, threshold, stone, deepslate));
    }

    private static void awaitGuestRules(ClientGameTestContext context, TestServerContext server, GuestProcess guest,
                                        int threshold, boolean stone, boolean deepslate) {
        await(context, "guest rules [threshold=" + threshold + ", stone=" + stone + ", deepslate=" + deepslate + "]",
                () -> {
                    guest.requireRunning();
                    return hasRules(server, GUEST, threshold, stone, deepslate);
                });
    }

    private static boolean hasRules(TestServerContext server, String name, int threshold, boolean stone, boolean deepslate) {
        return server.computeOnServer(mc -> {
            ServerPlayer player = mc.getPlayerList().getPlayerByName(name);
            if (player == null) return false;
            var rules = ServerMiningRules.forPlayer(player);
            return rules.requiredDurability(IRON_PICKAXE) == threshold
                    && rules.ignoresBlock(STONE) == stone && rules.ignoresBlock(DEEPSLATE) == deepslate;
        });
    }

    private static void checkMining(TestServerContext server, String name, Block block,
                                    int remainingDurability, boolean expectChain) {
        checkMining(server, name, block, remainingDurability, expectChain, false);
    }

    private static void checkMining(TestServerContext server, String name, Block block,
                                    int remainingDurability, boolean expectChain, boolean centerBlockPending) {
        checkMining(server, name, block, remainingDurability, expectChain, centerBlockPending, 1);
    }

    private static void checkMining(TestServerContext server, String name, Block block,
                                    int remainingDurability, boolean expectChain, boolean centerBlockPending,
                                    int damagePerBlock) {
        server.runOnServer(mc -> {
            ServerPlayer player = mc.getPlayerList().getPlayerByName(name);
            player.gameMode.changeGameModeForPlayer(GameType.SURVIVAL);
            if (!Utils.getVeinMineSwitchState(player)) Utils.toggleVeinMineSwitchState(player);
            var level = player.level();
            BlockPos origin = player.blockPosition().above(8);
            BlockPos neighbor = origin.east();
            ItemStack pickaxe = new ItemStack(Items.IRON_PICKAXE);
            pickaxe.setDamageValue(pickaxe.getMaxDamage() - remainingDurability);
            Tool tool = pickaxe.get(DataComponents.TOOL);
            if (tool.damagePerBlock() != damagePerBlock) {
                pickaxe.set(DataComponents.TOOL, new Tool(tool.rules(), tool.defaultMiningSpeed(), damagePerBlock,
                        tool.canDestroyBlocksInCreative()));
            }
            player.getInventory().setSelectedItem(pickaxe);
            level.setBlockAndUpdate(origin, block.defaultBlockState());
            level.setBlockAndUpdate(neighbor, block.defaultBlockState());
            if (centerBlockPending) {
                // Exercise the shared before-break path used by NeoForge.
                BlockBreak.beforeBlockBreak(level, player, origin, block.defaultBlockState());
                check(level.getBlockState(origin).is(block), "the before-break handler leaves the first block in place");
                level.setBlockAndUpdate(origin, Blocks.AIR.defaultBlockState());
            } else {
                check(player.gameMode.destroyBlock(origin), name + " can break the first " + block);
            }
            boolean chained = level.getBlockState(neighbor).isAir();
            level.setBlockAndUpdate(neighbor, Blocks.AIR.defaultBlockState());
            check(chained == expectChain, name + " chain result for " + block + " at durability " + remainingDurability
                    + " and damage per block " + damagePerBlock);
        });
    }

    private static void checkAir(TestServerContext server, String name) {
        server.runOnServer(mc -> {
            var player = mc.getPlayerList().getPlayerByName(name);
            for (Block air : Set.of(Blocks.AIR, Blocks.CAVE_AIR, Blocks.VOID_AIR)) {
                var found = SmartVein.findBlocks(player.level(), player.blockPosition(), air.defaultBlockState(), id -> false);
                check(found != null && found.isEmpty(), "exclude " + air + " independently of configured rules");
            }
        });
    }

    private static JsonObject readConfig() throws IOException {
        return JsonParser.parseString(Files.readString(CONFIG_FILE)).getAsJsonObject();
    }

    private static void reloadServerRuleMode(TestServerContext server, ServerRuleMode mode) throws IOException {
        JsonObject config = readConfig();
        config.addProperty("serverRuleMode", mode.name());
        Files.writeString(CONFIG_FILE, config.toString());
        server.runCommand("veinmine reload");
    }

    private static void chooseNextServerRuleMode(ClientGameTestContext context, ServerRuleMode startingMode,
                                                 ServerRuleMode expectedMode) throws IOException {
        openAdvancedSettings(context);
        context.runOnClient(client -> {
            var screen = (ConfigScreen) client.gui.screen();
            var button = serverRulesButton(client);
            check(button.active && button.getValue() == startingMode, "host can change the current server mode");
            client.setLastInputType(InputType.MOUSE);
            var click = new MouseButtonEvent(button.getX() + button.getWidth() / 2.0,
                    button.getY() + button.getHeight() / 2.0, new MouseButtonInfo(InputConstants.MOUSE_BUTTON_LEFT, 0));
            check(screen.mouseClicked(click, false), "server mode button accepts the click");
            screen.mouseReleased(click);
            check(button.getValue() == expectedMode && screen.hasUnsavedChanges(), "mode changes in the draft");
        });
        check(readConfig().get("serverRuleMode").getAsString().equals(startingMode.name()),
                "editing the draft does not change the config file");
        context.clickScreenButton("vm.config.save_and_exit");
        check(readConfig().get("serverRuleMode").getAsString().equals(expectedMode.name()),
                "saving writes the mode to the main config file");
    }

    private static void openAdvancedSettings(ClientGameTestContext context) {
        context.setScreen(() -> new ConfigScreen(null));
        context.clickScreenButton("vm.config.screen.final_resort");
        context.waitTick();
    }

    private static CycleButton<?> serverRulesButton(Minecraft client) {
        for (var child : client.gui.screen().children()) {
            if (child instanceof CycleButton<?> button && button.getValue() instanceof ServerRuleMode) {
                return button;
            }
        }
        throw new AssertionError("Server rule mode button was not found in Advanced Options");
    }

    static void await(ClientGameTestContext context, String description, BooleanSupplier condition) {
        long deadline = System.nanoTime() + CONNECTION_TIMEOUT.toNanos();
        while (!condition.getAsBoolean()) {
            check(System.nanoTime() < deadline, "timed out waiting for " + description);
            context.waitTick();
            try {
                Thread.sleep(10);
            } catch (InterruptedException error) {
                Thread.currentThread().interrupt();
                throw new AssertionError("Interrupted while waiting for " + description, error);
            }
        }
    }

    private static int freeLoopbackPort() throws IOException {
        try (var socket = new ServerSocket(0, 1, InetAddress.getByName("127.0.0.1"))) {
            return socket.getLocalPort();
        }
    }

    static void check(boolean condition, String description) {
        if (!condition) throw new AssertionError(description);
    }
}
