package top.diaoyugan.veinmine.config;

import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.sdl.SDLKeyboard;
import org.lwjgl.sdl.SDLScancode;

import java.util.ArrayList;
import java.util.List;

final class ConfigMigration {
    private static final int CURRENT_VERSION = 2;

    private ConfigMigration() {}

    static boolean migrate(ConfigItems config) {
        if (config.version >= CURRENT_VERSION) {
            return false;
        }

        migrateLegacyConfigScreenKeys(config);
        config.version = CURRENT_VERSION;
        return true;
    }

    private static void migrateLegacyConfigScreenKeys(ConfigItems config) {
        if (config.configScreenKey == null || config.configScreenKey.isEmpty()) {
            config.configScreenKey = new ArrayList<>(List.of("key.keyboard.v", "key.keyboard.m"));
            return;
        }

        List<String> migratedKeys = new ArrayList<>();
        for (String key : config.configScreenKey) {
            migratedKeys.add(fromLegacySerializedKey(key));
        }
        config.configScreenKey = migratedKeys;
    }

    private static String fromLegacySerializedKey(String key) {
        try {
            return fromLegacyGlfwPrintableKey(Integer.parseInt(key));
        } catch (NumberFormatException ignored) {
            return key;
        }
    }

    private static String fromLegacyGlfwPrintableKey(int key) {
        if (key < 32 || key > 126) {
            return Integer.toString(key);
        }

        int sdlKeyCode = Character.toLowerCase((char) key);
        int scanCode = SDLKeyboard.SDL_GetScancodeFromKey(sdlKeyCode, null);
        if (scanCode == SDLScancode.SDL_SCANCODE_UNKNOWN) {
            return Integer.toString(key);
        }

        return InputConstants.Type.KEYBOARD.getOrCreate(scanCode).getName();
    }
}
