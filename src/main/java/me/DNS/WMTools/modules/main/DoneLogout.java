package me.DNS.wmtools.modules.main;

import me.DNS.wmtools.WMTools;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.misc.AutoReconnect;
import net.minecraft.text.Text;

public class DoneLogout extends Module { 
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    public DoneLogout() {
        super(WMTools.MAIN, "HighwayCheck", "Logs out if there's another highway in front of the player.");
    }

    public enum BuildDirection {
        North, South, East, West
    }

    private final Setting<BuildDirection> Direction = sgGeneral.add(
        new EnumSetting.Builder<BuildDirection>()
            .name("Direction")
            .description("In what direction you build.")
            .defaultValue(BuildDirection.East)
            .build()
    );

    private final Setting<Double> range = sgGeneral.add(
        new DoubleSetting.Builder()
            .name("Range")
            .description("How many blocks in front of the player to check for obsidian.")
            .defaultValue(2.0)
            .min(1.0)
            .max(10.0)
            .sliderRange(1.0, 10.0)
            .build()
    );
/*
    private final Setting<Boolean> ToggleOnUse = sgGeneral.add(
        new BoolSetting.Builder()
            .name("ToggleOnUse")
            .description("Toggles the module on Use. Highly recommend as joining with it on can crash you")
            .defaultValue(true)
            .build()
    );
*/
    private final Setting<Boolean> ToggleautoReconnect = sgGeneral.add(
        new BoolSetting.Builder()
            .name("AutoReconnect")
            .description("Disables AutoReconnect when logging out.")
            .defaultValue(true)
            .build()
    );

    private void disconnect(String message) {
        if (mc.player != null && mc.getNetworkHandler() != null) {
            mc.getNetworkHandler().getConnection().disconnect(Text.literal(message));
        }
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.player == null || mc.world == null) return;

        BlockPos playerPos = mc.player.getBlockPos();
        Direction selectedDirection = getDirectionFromSetting(Direction.get());
        BlockPos centerPos = playerPos.offset(selectedDirection, range.get().intValue()).down();

        Direction sideDir = selectedDirection.rotateYClockwise();

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

            info("5-wide obsidian block detected in front. Logging out at: " + coords);

            disconnect("5-wide obsidian block detected, " + coords);
            /*
            if (ToggleOnUse.get()) {
                toggle();
            }
            */
            if (ToggleautoReconnect.get() && Modules.get().isActive(AutoReconnect.class)) Modules.get().get(AutoReconnect.class).toggle();
        }
    }

    private net.minecraft.util.math.Direction getDirectionFromSetting(BuildDirection dir) {
        return switch (dir) {
            case North -> net.minecraft.util.math.Direction.NORTH;
            case South -> net.minecraft.util.math.Direction.SOUTH;
            case East -> net.minecraft.util.math.Direction.EAST;
            case West -> net.minecraft.util.math.Direction.WEST;
        };
    }
}