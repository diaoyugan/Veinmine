package top.diaoyugan.veinmine.client.configScreen;

import net.minecraft.network.chat.Component;

public class PendingTab {
    int x, y, height;
    Component text;
    Runnable action;

    PendingTab(int x, int y, int height, Component text, Runnable action) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.text = text;
        this.action = action;
    }
}

