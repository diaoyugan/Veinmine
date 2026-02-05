package top.diaoyugan.veinmine.client.configScreen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.Screen;
import top.diaoyugan.veinmine.client.configScreen.widget.TitleWidget;
import top.diaoyugan.veinmine.config.Config;

import net.minecraft.network.chat.Component;
import top.diaoyugan.veinmine.config.ConfigItems;

import java.util.ArrayList;
import java.util.List;

public class ConfigScreen extends Screen {

    private final Screen parent;
    private final ConfigItems items = Config.getInstance().getConfigItems();

    private final List<List<AbstractWidget>> pages = new ArrayList<>();
    private int currentPage = 0;

    private ConfigMainPage mainPage;

    public ConfigScreen(Screen parent) {
        super(Component.translatable("vm.config.screen.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = width / 2;

        pages.clear();

        // Page 1
        mainPage = new ConfigMainPage(items);
        pages.add(mainPage.build(centerX));

        // Page 2
        List<AbstractWidget> page2 = new ArrayList<>();
        page2.add(new TitleWidget(
                centerX - 100,
                30,
                Component.translatable("vm.config.screen.toolsandprotect")
        ));
        pages.add(page2);

        // Page 3
        List<AbstractWidget> page3 = new ArrayList<>();
        page3.add(new TitleWidget(
                centerX - 100,
                30,
                Component.translatable("vm.config.screen.highlights")
        ));
        pages.add(page3);

        // 顶部翻页按钮
        addRenderableWidget(Button.builder(
                        Component.translatable("vm.config.screen.main"),
                        b -> showPage(0))
                .bounds(10, 10, 60, 20)
                .build());

        addRenderableWidget(Button.builder(
                        Component.translatable("vm.config.screen.toolsandprotect"),
                        b -> showPage(1))
                .bounds(10, 34, 60, 20)
                .build());

        addRenderableWidget(Button.builder(
                        Component.translatable("vm.config.screen.highlights"),
                        b -> showPage(2))
                .bounds(10, 58, 60, 20)
                .build());

        int bottomY = height - 28;

        addRenderableWidget(Button.builder(
                        Component.translatable("vm.config.cancel"),
                        b -> onClose())
                .bounds(centerX - 154, bottomY, 150, 20)
                .build());

        addRenderableWidget(Button.builder(
                        Component.translatable("vm.config.save_and_exit"),
                        b -> saveAndExit())
                .bounds(centerX + 4, bottomY, 150, 20)
                .build());

        showPage(0);
    }

    private void showPage(int index) {
        if (currentPage < pages.size()) {
            for (AbstractWidget w : pages.get(currentPage)) {
                removeWidget(w);
            }
        }

        for (AbstractWidget w : pages.get(index)) {
            addRenderableWidget(w);
        }

        currentPage = index;
    }

    private void saveAndExit() {
        mainPage.save();
        Config.getInstance().save();
        minecraft.setScreen(parent);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float delta) {
        super.render(g, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        minecraft.setScreen(parent);
    }
}
