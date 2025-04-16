package me.redcarlos.higtools.modules.main;

import me.redcarlos.higtools.HIGTools;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.Settings;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class DoneLogout extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> range = sgGeneral.add(new DoubleSetting.Builder()
        .name("Range")
        .description("How many blocks in front of the player to check for obsidian.")
        .defaultValue(2)
        .min(1)
        .max(10)
        .sliderRange(1, 10)
        .build()
    );

    public DoneLogout() {
        super(HIGTools.MAIN, "HighwayCheck", "Logs out if there's another highway in front of the player.");
    }

@EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.player == null || mc.world == null) return;

        Direction facing = mc.player.getHorizontalFacing();
        BlockPos playerPos = mc.player.getBlockPos();
        BlockPos centerPos = playerPos.offset(facing).down(); // Middle block in front

        // Determine axis perpendicular to facing direction (left/right direction)
        Direction sideDir = facing.rotateYClockwise(); // Rotate 90° clockwise to get perpendicular

        boolean allObsidian = true;

        for (int i = -2; i <= 2; i++) {
            BlockPos checkPos = centerPos.offset(sideDir, i);

            if (!mc.world.getBlockState(checkPos).isOf(Blocks.OBSIDIAN)) {
                allObsidian = false;
                break;
            }
        }

        if (allObsidian) {
            String coords = String.format("Logged at X: %d, Y: %d, Z: %d",
                playerPos.getX(), playerPos.getY(), playerPos.getZ());

            info("5-wide obsidian block detected in front. Logging out.");
            info(coords);

            mc.world.disconnect();
            mc.disconnect(); // Clean up
            mc.setScreen(new TitleScreen()); // Go to menu
        }
    }
}
