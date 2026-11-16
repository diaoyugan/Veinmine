package top.diaoyugan.veinmine.client.configScreen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import top.diaoyugan.enchanted_ui.api.client.gui.UIVirtualList;
import top.diaoyugan.enchanted_ui.api.client.gui.layout.UIBounds;
import top.diaoyugan.enchanted_ui.api.client.gui.theme.UITheme;
import top.diaoyugan.enchanted_ui.api.client.gui.theme.UIThemes;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

final class RegistryPickerScreen extends Screen {
    enum Kind { TOOLS, BLOCKS }

    private static final int ROW_HEIGHT = 25;
    private static final Comparator<Entry> BY_NAME = Comparator.comparing(Entry::searchName).thenComparing(Entry::id);

    private final Screen parent;
    private final Kind kind;
    private final boolean canBrowse;
    private final Map<String, Entry> entriesById = new HashMap<>();
    private final Set<String> selected;
    private final Consumer<Set<String>> onDone;
    private final UITheme theme = UIThemes.current();
    private List<Entry> entries;
    private boolean selectedOnly;
    private String query = "";
    private String manualId = "";
    private EditBox idInput;
    private Button addIdButton;

    RegistryPickerScreen(Screen parent, Component title, Kind kind, boolean canBrowse,
                         List<String> selected, Consumer<Set<String>> onDone) {
        super(title);
        this.parent = parent;
        this.kind = kind;
        this.canBrowse = canBrowse;
        this.selected = new LinkedHashSet<>(selected);
        this.onDone = onDone;
        if (canBrowse) loadCatalog();
        this.selected.forEach(this::findEntry);
        refreshEntries();
    }

    private void loadCatalog() {
        if (kind == Kind.BLOCKS) {
            BuiltInRegistries.BLOCK.listElements().forEach(holder -> findEntry(holder.key().identifier().toString()));
            return;
        }
        BuiltInRegistries.ITEM.listElements()
                .filter(Holder.Reference::areComponentsBound)
                .forEach(holder -> {
                    ItemStack stack = holder.value().getDefaultInstance();
                    if (stack.isDamageableItem()) {
                        String id = holder.key().identifier().toString();
                        entriesById.put(id, new Entry(id, stack.getHoverName(), stack));
                    }
                });
    }

    private Entry findEntry(String id) {
        return entriesById.computeIfAbsent(id, this::resolveEntry);
    }

    private Entry resolveEntry(String id) {
        Identifier identifier = Identifier.tryParse(id);
        if (identifier != null) {
            if (kind == Kind.BLOCKS) {
                var block = BuiltInRegistries.BLOCK.get(identifier);
                if (block.isPresent()) {
                    var value = block.get().value();
                    return new Entry(id, value.getName(), blockIcon(value.asItem()));
                }
            } else {
                var item = BuiltInRegistries.ITEM.get(identifier);
                if (item.isPresent()) {
                    Item value = item.get().value();
                    ItemStack icon = item.get().areComponentsBound() ? value.getDefaultInstance() : ItemStack.EMPTY;
                    Component name = icon.isEmpty() ? Component.translatable(value.getDescriptionId()) : icon.getHoverName();
                    return new Entry(id, name, icon);
                }
            }
        }
        return new Entry(id, Component.translatable("vm.config.picker.unknown"), ItemStack.EMPTY);
    }

    private void refreshEntries() {
        entries = (!canBrowse || selectedOnly ? selected.stream().map(this::findEntry) : entriesById.values().stream())
                .sorted(BY_NAME)
                .toList();
    }

    private static ItemStack blockIcon(Item item) {
        if (item == Items.AIR) return ItemStack.EMPTY;
        return BuiltInRegistries.ITEM.get(BuiltInRegistries.ITEM.getKey(item))
                .filter(Holder.Reference::areComponentsBound)
                .map(holder -> item.getDefaultInstance())
                .orElse(ItemStack.EMPTY);
    }

    @Override
    protected void init() {
        int contentWidth = Math.min(380, width - 24);
        int left = (width - contentWidth) / 2;
        int listTop = 76;
        UIVirtualList<Entry, String> list = UIVirtualList.<Entry, String>builder(Entry::id)
                .label(title)
                .bounds(left, listTop, contentWidth, Math.max(1, height - listTop - (canBrowse ? 38 : 49)))
                .items(() -> entries)
                .filter((entry, search) -> entry.searchName().contains(search) || entry.id().contains(search))
                .rowHeight(ROW_HEIGHT)
                .row(this::renderEntry)
                .rowAction(contentWidth, (entry, mouseButton) -> toggle(entry))
                .onActivate(this::toggle)
                .theme(theme)
                .build()
                .query(normalizedQuery());

        int filterWidth = canBrowse ? Math.min(115, contentWidth / 3) : 0;
        EditBox search = input(left, 28, contentWidth - (canBrowse ? filterWidth + 4 : 0),
                "vm.config.picker.search", query);
        search.setResponder(value -> {
            query = value;
            list.query(normalizedQuery());
        });
        if (canBrowse) {
            addRenderableWidget(Button.builder(filterLabel(), button -> {
                selectedOnly = !selectedOnly;
                button.setMessage(filterLabel());
                refreshEntries();
                list.query(normalizedQuery());
            }).bounds(left + contentWidth - filterWidth, 28, filterWidth, 20).build());
        }

        int addWidth = Math.min(80, contentWidth / 3);
        idInput = input(left, 52, contentWidth - addWidth - 4, "vm.config.picker.id", manualId);
        addIdButton = addRenderableWidget(Button.builder(Component.translatable("vm.config.picker.add_id"), button -> {
            Identifier id = parseManualId();
            if (id == null || !selected.add(id.toString())) return;
            findEntry(id.toString());
            refreshEntries();
            search.setValue("");
            list.scrollToKey(id.toString());
            idInput.setValue("");
        }).bounds(left + contentWidth - addWidth, 52, addWidth, 20).build());
        idInput.setResponder(value -> {
            manualId = value;
            updateAddButton();
        });
        updateAddButton();
        addRenderableWidget(list);

        int buttonWidth = Math.min(120, (contentWidth - 8) / 2);
        addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, button -> onClose())
                .bounds(width / 2 - buttonWidth - 4, height - 28, buttonWidth, 20).build());
        addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> {
            onDone.accept(selected);
            onClose();
        }).bounds(width / 2 + 4, height - 28, buttonWidth, 20).build());
    }

    private EditBox input(int x, int y, int width, String hintKey, String value) {
        Component hint = Component.translatable(hintKey);
        EditBox input = new EditBox(font, x, y, width, 20, hint);
        input.setMaxLength(256);
        input.setHint(hint);
        input.setValue(value);
        return addRenderableWidget(input);
    }

    private Component filterLabel() {
        return Component.translatable(selectedOnly ? "vm.config.picker.show_all" : "vm.config.picker.show_selected");
    }

    private Identifier parseManualId() {
        Identifier id = Identifier.tryParse(manualId.strip());
        return id == null || id.getPath().isEmpty() ? null : id;
    }

    private void updateAddButton() {
        Identifier id = parseManualId();
        addIdButton.active = id != null && !selected.contains(id.toString());
        Component error = null;
        if (!manualId.isBlank() && !addIdButton.active) {
            error = Component.translatable(id == null ? "vm.config.picker.invalid_id" : "vm.config.screen.duplicate_entry");
        }
        idInput.setTooltip(error == null ? null : Tooltip.create(error));
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics) {
        super.extractRenderState(graphics);
        graphics.centeredText(font, title, width / 2, 12, theme.colors().text());
        if (!canBrowse) {
            graphics.centeredText(font, Component.translatable("vm.config.picker.requires_world"),
                    width / 2, height - 44, theme.colors().textMuted());
        }
    }

    @Override
    public void onClose() {
        minecraft.setScreenAndShow(parent);
    }

    private String normalizedQuery() {
        return query.strip().toLowerCase(Locale.ROOT);
    }

    private void toggle(Entry entry) {
        if (!selected.remove(entry.id())) selected.add(entry.id());
        refreshEntries();
        updateAddButton();
    }

    private void renderEntry(GuiGraphicsExtractor graphics, UIBounds row, Entry entry, UIVirtualList.RowState state) {
        UITheme.Colors colors = theme.colors();
        int top = row.y();
        int left = row.x();
        int right = left + row.width();
        boolean checked = selected.contains(entry.id());
        int background = state.focused() || state.hovered() ? colors.surfaceHovered() : colors.surface();
        graphics.fill(left, top, right, top + ROW_HEIGHT, background);
        if (entry.icon().isEmpty()) {
            graphics.outline(left + 5, top + 5, 14, 14, colors.border());
        } else {
            graphics.fakeItem(entry.icon(), left + 4, top + 4);
        }

        int checkX = right - 19;
        graphics.outline(checkX, top + 6, 13, 13, checked ? colors.accent() : colors.textMuted());
        if (checked) {
            graphics.text(font, "✓", checkX + 3, top + 8, colors.text(), false);
        }

        graphics.enableScissor(left + 24, top, checkX - 4, top + ROW_HEIGHT);
        graphics.text(font, entry.name(), left + 24, top + 3, colors.text(), false);
        graphics.text(font, entry.id(), left + 24, top + 14, colors.textMuted(), false);
        graphics.disableScissor();
    }

    private record Entry(String id, Component name, ItemStack icon, String searchName) {
        private Entry(String id, Component name, ItemStack icon) {
            this(id, name, icon, name.getString().toLowerCase(Locale.ROOT));
        }
    }
}
