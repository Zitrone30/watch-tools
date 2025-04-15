package me.redcarlos.higtools.modules.main;

import me.redcarlos.higtools.HIGTools;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.network.ClientPlayerEntity;

public class Gravity extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> upVelocity = sgGeneral.add(new DoubleSetting.Builder()
        .name("up-velocity")
        .description("Upward velocity applied to the player.")
        .defaultValue(0.0)
        .min(-10.0)
        .max(10.0)
        .sliderRange(-10.0, 10.0)
        .build()
    );

    private final Setting<Double> downVelocity = sgGeneral.add(new DoubleSetting.Builder()
        .name("down-velocity")
        .description("Downward velocity applied to the player.")
        .defaultValue(0.0)
        .min(-10.0)
        .max(10.0)
        .sliderRange(-10.0, 10.0)
        .build()
    );

    private final Setting<Double> xVelocity = sgGeneral.add(new DoubleSetting.Builder()
        .name("x-velocity")
        .description("Horizontal X-axis velocity applied to the player.")
        .defaultValue(0.0)
        .min(-10.0)
        .max(10.0)
        .sliderRange(-10.0, 10.0)
        .build()
    );

    private final Setting<Double> zVelocity = sgGeneral.add(new DoubleSetting.Builder()
        .name("z-velocity")
        .description("Horizontal Z-axis velocity applied to the player.")
        .defaultValue(0.0)
        .min(-10.0)
        .max(10.0)
        .sliderRange(-10.0, 10.0)
        .build()
    );

    public Gravity() {
        super(HIGTools.MAIN, "Forces", "Modify the forces put upon the player.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        ClientPlayerEntity player = mc.player;
        if (player == null) return;

        double yVelocity = upVelocity.get() - downVelocity.get();
        double xVel = xVelocity.get();
        double zVel = zVelocity.get();

        player.setVelocity(xVel, yVelocity, zVel);
    }
}

