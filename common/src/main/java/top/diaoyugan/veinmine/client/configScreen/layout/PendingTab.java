package top.diaoyugan.veinmine.client.configScreen.layout;

import net.minecraft.network.chat.Component;

public class PendingTab {
    int index;
    public int x;
    public int y;
    public int height;
    public Component text;
    public Runnable action;

    public PendingTab(int x, int y, int height, Component text, Runnable action, int index) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.text = text;
        this.action = action;
        this.index = index;
    }
}

