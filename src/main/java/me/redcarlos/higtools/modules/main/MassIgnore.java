package me.DNS.vmtools.modules.main;

import me.DNS.higtools.WMTools;
import meteordevelopment.meteorclient.events.game.SendMessageEvent;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.StringListSetting;
import meteordevelopment.meteorclient.settings.StringSetting;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

import java.util.List;

public class MassIgnore extends Module {
    final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<List<String>> players = sgGeneral.add(new StringListSetting.Builder()
        .name("players")
        .description("Players to message.")
        .build()
    );

    private final Setting<String> command = sgGeneral.add(new StringSetting.Builder()
        .name("command")
        .description("How the message command is set up on the server.")
        .defaultValue("/ignore")
        .build()
    );

    public MassIgnore() {
        super(HIGTools.MAIN, "MassIgnore", "Ignore or Unignore alot of people at the same time");

    }

    @EventHandler
    private void onmessage(SendMessageEvent event){
        event.cancel();
        for (String p : players.get()){
            mc.player.networkHandler.sendCommand(command + " " + p + " " + event.message);
        }
    }
}
