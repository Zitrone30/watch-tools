package me.DNS.vmtools.modules.main;

import me.DNS.higtools.WMTools;
import meteordevelopment.meteorclient.events.game.SendMessageEvent;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.StringListSetting;
import meteordevelopment.meteorclient.settings.StringSetting;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import me.DNS.higtools.WMTools;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.common.DisconnectS2CPacket;
import net.minecraft.text.Text;
import me.DNS.vmtools.utils.KickEvent;

import java.util.List;
import java.util.Random;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class Excuse extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> minTotem = sgGeneral.add(new IntSetting.Builder()
        .name("min-totems")
        .defaultValue(2)
        .min(1)
        .sliderRange(1, 15)
        .build()
    );
    private final Setting<Integer> minHealth = sgGeneral.add(new IntSetting.Builder()
        .name("min-health")
        .defaultValue(5)
        .min(1)
        .sliderRange(1, 36)
        .build()
    );
    private final Setting<Boolean> toggleKick = sgGeneral.add(new BoolSetting.Builder()
        .name("toggle-kick")
        .defaultValue(true)
        .build()
    );
    private final Setting<List<String>> msges = sgGeneral.add(new StringListSetting.Builder()
        .name("kick-messages")
        .defaultValue("Illegal client modifications",
            "Unlikely fast clicking",
            "Timed out"
        )
        .build()
    );

    public Excuse() {
        super(HIGTools.MAIN, "ExcuseLog", "Log out of the server with an excuse.");
    }

    @EventHandler
    public void onTick(TickEvent.Pre event) {
        FindItemResult totem = InvUtils.find(Items.TOTEM_OF_UNDYING);
        if (totem.count() < minTotem.get() || PlayerUtils.getTotalHealth() <= minHealth.get()) {
            assert mc.player != null;

            Random random = new Random();
            String msg = msges.get().get(random.nextInt(msges.get().size()));

            mc.player.networkHandler.onDisconnect(new DisconnectS2CPacket(Text.literal(msg)));

        }
    }
    @EventHandler
    public void onKick(KickEvent event){
        if(!toggleKick.get()) return;
    }
}
