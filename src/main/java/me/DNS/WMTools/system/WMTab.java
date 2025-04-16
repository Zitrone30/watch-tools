package me.DNS.wmtools.system;

import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.tabs.Tab;
import meteordevelopment.meteorclient.gui.tabs.TabScreen;
import meteordevelopment.meteorclient.gui.tabs.WindowTabScreen;
import meteordevelopment.meteorclient.settings.Settings;
import meteordevelopment.meteorclient.utils.misc.NbtUtils;
import net.minecraft.client.gui.screen.Screen;

public class WMTab extends Tab {
    public WMTab() {
        super("WM Tools");
    }

    @Override
    public TabScreen createScreen(GuiTheme theme) {
        return new WMScreen(theme, this);
    }

    @Override
    public boolean isScreen(Screen screen) {
        return screen instanceof WMScreen;
    }

    private static class WMScreen extends WindowTabScreen {
        private final Settings settings;

        public WMScreen(GuiTheme theme, Tab tab) {
            super(theme, tab);
            settings = WMSystem.get().settings;
            settings.onActivated();
        }

        @Override
        public void initWidgets() {
            add(theme.settings(settings)).expandX();
        }

        @Override
        public void tick() {
            super.tick();
            settings.tick(window, theme);
        }

        @Override
        public boolean toClipboard() {
            return NbtUtils.toClipboard(WMSystem.get());
        }

        @Override
        public boolean fromClipboard() {
            return NbtUtils.fromClipboard(WMSystem.get());
        }
    }
}
