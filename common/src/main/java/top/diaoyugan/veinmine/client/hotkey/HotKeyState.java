package top.diaoyugan.veinmine.client.hotkey;

public final class HotKeyState {

    private static boolean veinMineSwitchState = false;
    private static boolean lastPressed = false;

    private HotKeyState() {}

    // 服务端同步结果
    public static void updateFromServer(boolean state) {
        veinMineSwitchState = state;
    }

    public static boolean isVeinMineEnabled() {
        return veinMineSwitchState;
    }

    public static boolean consumeLastPressedChange(boolean currentPressed) {
        if (currentPressed != lastPressed) {
            lastPressed = currentPressed;
            return true;
        }
        return false;
    }
}
