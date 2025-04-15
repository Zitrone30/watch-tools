package me.redcarlos.higtools;

import com.mojang.logging.LogUtils;

import me.redcarlos.higtools.commands.Center;
import me.redcarlos.higtools.commands.Coordinates;
import me.redcarlos.higtools.commands.Panorama;
import me.redcarlos.higtools.modules.highwayborers.AxisBorer;
import me.redcarlos.higtools.modules.highwayborers.NegNegBorer;
import me.redcarlos.higtools.modules.highwayborers.NegPosBorer;
import me.redcarlos.higtools.modules.highwayborers.PosNegBorer;
import me.redcarlos.higtools.modules.highwayborers.PosPosBorer;
import me.redcarlos.higtools.modules.hud.TextPresets;
import me.redcarlos.higtools.modules.main.AdvancedPlace;
import me.redcarlos.higtools.modules.main.AfkLogout;
import me.redcarlos.higtools.modules.main.AntiToS;
import me.redcarlos.higtools.modules.main.AutoCenter;
import me.redcarlos.higtools.modules.main.AutoDoors;
import me.redcarlos.higtools.modules.main.AutoLogPlus;
import me.redcarlos.higtools.modules.main.AutoWalkHIG;
import me.redcarlos.higtools.modules.main.AutoWalkP;
import me.redcarlos.higtools.modules.main.AxisViewer;
import me.redcarlos.higtools.modules.main.BedrockWalk;
import me.redcarlos.higtools.modules.main.BoomView;
import me.redcarlos.higtools.modules.main.Conditions;
import me.redcarlos.higtools.modules.main.DiscordRPC;
import me.redcarlos.higtools.modules.main.DoneLogout;
import me.redcarlos.higtools.modules.main.Excuse;
import me.redcarlos.higtools.modules.main.Gravity;
import me.redcarlos.higtools.modules.main.Groupmessage;
import me.redcarlos.higtools.modules.main.HighwayBuilderHIG;
import me.redcarlos.higtools.modules.main.HighwayTools;
import me.redcarlos.higtools.modules.main.HitboxDesync;
import me.redcarlos.higtools.modules.main.HotbarManager;
import me.redcarlos.higtools.modules.main.Jitter;
import me.redcarlos.higtools.modules.main.LiquidFillerHIG;
import me.redcarlos.higtools.modules.main.MassIgnore;
import me.redcarlos.higtools.modules.main.NoPingDif;
import me.redcarlos.higtools.modules.main.OffhandManager;
import me.redcarlos.higtools.modules.main.PermJukebox;
import me.redcarlos.higtools.modules.main.Pulse;
import me.redcarlos.higtools.modules.main.RangeCMD;
import me.redcarlos.higtools.modules.main.SafetyNet;
import me.redcarlos.higtools.modules.main.ScaffoldHIG;
import me.redcarlos.higtools.modules.main.StopDrop;
import me.redcarlos.higtools.modules.main.StreamerMode;
import me.redcarlos.higtools.system.HIGTab;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.gui.tabs.Tabs;
import meteordevelopment.meteorclient.systems.Systems;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.hud.HudGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.misc.BetterChat;
import meteordevelopment.meteorclient.utils.misc.MeteorStarscript;
import meteordevelopment.starscript.value.ValueMap;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class HIGTools extends MeteorAddon {
    public static final String MOD_ID = "higtools";
    public static final ModMetadata METADATA;
    public static final String VERSION;
    public static final Category MAIN;
    public static final Category BORERS;
    public static final Category WATCHPVP;
    public static final HudGroup HUD;

    static {
        METADATA = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow().getMetadata();
        VERSION = METADATA.getVersion().getFriendlyString();

        MAIN = new Category("Watchmen Additions", Items.BEACON.getDefaultStack());
        BORERS = new Category("  Watchmen Borers  ", Items.NETHERITE_PICKAXE.getDefaultStack());
        WATCHPVP = new Category("Watchmen Offense", Items.NETHERITE_SWORD.getDefaultStack());
        HUD = new HudGroup("Watchmen");
    }

    @Override
    public void onInitialize() {
        LogUtils.getLogger().info("Initializing Watchmen Additions {}", HIGTools.VERSION);

        // Systems
        BetterChat.registerCustomHead("~Watchmen~", identifier("icon.png"));
        MeteorStarscript.ss.set("Watchmen", new ValueMap().set("version", VERSION));
        Tabs.add(new HIGTab());

        // Commands
        Commands.add(new Center());
        Commands.add(new Coordinates());
        Commands.add(new Panorama());

        // Hud
        Hud hud = Systems.get(Hud.class);
        hud.register(TextPresets.INFO);

        // Modules
        Modules modules = Modules.get();

        modules.add(new DoneLogout());
        modules.add(new AfkLogout());
        modules.add(new AutoCenter());
        modules.add(new AutoWalkHIG());
        modules.add(new AxisViewer());
        modules.add(new DiscordRPC());
        modules.add(new HighwayBuilderHIG());
        modules.add(new HighwayTools());
        modules.add(new HotbarManager());
        modules.add(new LiquidFillerHIG());
        modules.add(new OffhandManager());
        modules.add(new ScaffoldHIG());
        modules.add(new PermJukebox());
        modules.add(new Groupmessage());
        modules.add(new MassIgnore());
        modules.add(new RangeCMD());
        modules.add(new NoPingDif());
        modules.add(new BoomView());
        modules.add(new AutoLogPlus());
        modules.add(new AdvancedPlace());
        modules.add(new Conditions());
        modules.add(new SafetyNet());
        modules.add(new Gravity());
        modules.add(new Jitter());
        modules.add(new Pulse());
        modules.add(new StopDrop());
        modules.add(new Excuse());
        modules.add(new BedrockWalk());
        modules.add(new AntiToS());
        modules.add(new AutoDoors());
        modules.add(new StreamerMode());
        modules.add(new AutoWalkP());
        modules.add(new HitboxDesync());
        // Borers
        modules.add(new AxisBorer());
        modules.add(new NegNegBorer());
        modules.add(new NegPosBorer());
        modules.add(new PosNegBorer());
        modules.add(new PosPosBorer());
    }

    @Override
    public String getPackage() {
        return "me.redcarlos.higtools";
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(MAIN);
        Modules.registerCategory(BORERS);
        Modules.registerCategory(WATCHPVP);
    }

    public static Identifier identifier(String path) {
        return Identifier.of(MOD_ID, path);
    }
}
