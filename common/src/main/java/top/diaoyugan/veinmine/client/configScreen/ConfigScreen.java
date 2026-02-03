package top.diaoyugan.veinmine.client.configScreen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import top.diaoyugan.veinmine.client.configScreen.widget.BooleanOptionWidget;
import top.diaoyugan.veinmine.client.configScreen.widget.TitleWidget;
import top.diaoyugan.veinmine.config.Config;

import net.minecraft.network.chat.Component;
import top.diaoyugan.veinmine.config.ConfigItems;

public class ConfigScreen extends Screen {
    ConfigItems items = Config.getInstance().getConfigItems();
    boolean uiUseBFS = items.useBFS;

    private final Screen parent;

    public ConfigScreen(Screen parent) {
        super(Component.translatable("vm.config.screen.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = width / 2;
        int y = 32;

        //标题
        addRenderableWidget(
                new TitleWidget(
                        centerX - 100,
                        y,
                        Component.translatable("vm.config.screen.main")
                )
        );
        y += 16;

        //配置项
        addRenderableWidget(
                new BooleanOptionWidget(
                        centerX - 100,
                        y,
                        200,
                        20,
                        Component.translatable("vm.config.use_bfs"),
                        () -> uiUseBFS,
                        v -> uiUseBFS = v
                )
        );


        int btnY = height - 28;

        addRenderableWidget(
                Button.builder(
                        Component.translatable("vm.config.cancel"),
                        b -> discardAndExit()
                ).bounds(width / 2 - 154, btnY, 150, 20).build()
        );
        addRenderableWidget(
                Button.builder(
                        Component.translatable("vm.config.save_and_exit"),
                        b -> saveAndExit()
                ).bounds(width / 2 + 4, btnY, 150, 20).build()
        );
    }

    private void saveAndExit() {
        ConfigItems items = Config.getInstance().getConfigItems();
        items.useBFS = uiUseBFS;

        Config.getInstance().save();

        if (minecraft != null) {
            minecraft.setScreen(parent);
        }
    }

    private void discardAndExit() {
        if (minecraft != null) {
            minecraft.setScreen(parent);
        }
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float delta) {
        //renderBackground(g,mouseX,mouseY,delta);
        super.render(g, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        minecraft.setScreen(parent);
    }
}


