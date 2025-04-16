package me.DNS.vmtools.modules.main;

import me.DNS.higtools.WMTools;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import meteordevelopment.meteorclient.settings.BoolSetting;

import java.util.Random;

public class Jitter extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> useRandom = sgGeneral.add(new BoolSetting.Builder()
        .name("random-jitter")
        .description("Whether to jitter with random movement or fixed values.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Double> jitterX = sgGeneral.add(new DoubleSetting.Builder()
        .name("x-jitter")
        .description("Maximum horizontal jitter (left/right).")
        .defaultValue(0.2)
        .min(0.0)
        .max(2.0)
        .sliderRange(0.0, 2.0)
        .build()
    );

    private final Setting<Double> jitterY = sgGeneral.add(new DoubleSetting.Builder()
        .name("y-jitter")
        .description("Maximum vertical jitter (up/down).")
        .defaultValue(0.2)
        .min(0.0)
        .max(2.0)
        .sliderRange(0.0, 2.0)
        .build()
    );

    private final Setting<Double> jitterZ = sgGeneral.add(new DoubleSetting.Builder()
        .name("z-jitter")
        .description("Maximum depth jitter (forward/backward).")
        .defaultValue(0.2)
        .min(0.0)
        .max(2.0)
        .sliderRange(0.0, 2.0)
        .build()
    );

    private final Setting<Integer> jitterSpeed = sgGeneral.add(new IntSetting.Builder()
        .name("jitter-speed")
        .description("How many ticks between jitter updates.")
        .defaultValue(5)
        .min(1)
        .max(20)
        .sliderRange(1, 20)
        .build()
    );

    private final Random random = new Random();
    private int tickCounter = 0;

    private double currentX = 0;
    private double currentY = 0;
    private double currentZ = 0;

    public Jitter() {
        super(HIGTools.MAIN, "Jitter", "Applies random jitter movement to the player.");
    }

    private double toggleDirection(double current, double amount) {
        return current >= 0 ? -amount : amount;
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        ClientPlayerEntity player = mc.player;
        if (player == null) return;

        tickCounter++;
        if (tickCounter >= jitterSpeed.get()) {
            tickCounter = 0;

            if (useRandom.get()) {
                currentX = getRandomJitter(jitterX.get());
                currentY = getRandomJitter(jitterY.get());
                currentZ = getRandomJitter(jitterZ.get());
            } else {
                currentX = toggleDirection(currentX, jitterX.get());
                currentY = toggleDirection(currentY, jitterY.get());
                currentZ = toggleDirection(currentZ, jitterZ.get());
            }
        }

        player.setVelocity(currentX, currentY, currentZ);
    }

    private double getRandomJitter(double max) {
        return (random.nextDouble() * 2 - 1) * max;
    }
}
