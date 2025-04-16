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

        for (int i = 1; i <= range.get(); i++) {
            BlockPos checkPos = mc.player.getBlockPos().offset(facing, i).down();

            if (mc.world.getBlockState(checkPos).isOf(Blocks.OBSIDIAN)) {
                BlockPos logoutPos = mc.player.getBlockPos();
                String coords = String.format("Logged at X: %d, Y: %d, Z: %d",
                        logoutPos.getX(), logoutPos.getY(), logoutPos.getZ());

                info("Obsidian detected " + i + " blocks ahead. Logging out.");
                info(coords);


                mc.world.disconnect();
                mc.disconnect(); // Cleans up network connection
                mc.setScreen(new TitleScreen()); // Return to main menu
                break;
            }
        }
    }
}
