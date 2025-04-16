package me.DNS.wmtools.modules.main;

import me.DNS.wmtools.WMTools;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.sound.SoundEvents;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Vec3d;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.minecraft.util.math.Box;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.sound.SoundEvents.BLOCK_NOTE_BLOCK_PLING;

public class Pulse extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> pulseInterval = sgGeneral.add(new IntSetting.Builder()
        .name("pulse-interval")
        .description("Time in ticks before toggling blink on/off.")
        .defaultValue(40)
        .min(1)
        .sliderMax(200)
        .build()
    );

    private boolean blinking = false;
    private int tickCounter = 0;
    private final List<Packet<?>> savedPackets = new ArrayList<>();

    public Pulse() {
        super(WMTools.MAIN, "Pulse Blink", "Blinks on and off at intervals.");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        tickCounter++;

        if (tickCounter >= pulseInterval.get()) {
            tickCounter = 0;
            blinking = !blinking;

            // If blinking is turning OFF, flush packets
            if (!blinking) {
                for (Packet<?> packet : savedPackets) {
                    mc.getNetworkHandler().sendPacket(packet);
                }
                savedPackets.clear();
            }
        }
    }


    @EventHandler
    private void onPacketSend(PacketEvent.Send event) {
        if (!blinking) return;

        // Intercept and store the packet
        event.cancel();
        savedPackets.add(event.packet);
    }

    @Override
    public void onDeactivate() {
        // Flush all packets when module is turned off
        for (Packet<?> packet : savedPackets) {
            mc.getNetworkHandler().sendPacket(packet);
        }
        savedPackets.clear();
        blinking = false;
        tickCounter = 0;
    }
}
