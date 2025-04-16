package me.DNS.wmtools;

import com.mojang.logging.LogUtils;

import me.DNS.wmtools.commands.Center;
import me.DNS.wmtools.commands.Coordinates;
import me.DNS.wmtools.commands.Panorama;
import me.DNS.wmtools.modules.highwayborers.AxisBorer;
import me.DNS.wmtools.modules.highwayborers.NegNegBorer;
import me.DNS.wmtools.modules.highwayborers.NegPosBorer;
import me.DNS.wmtools.modules.highwayborers.PosNegBorer;
import me.DNS.wmtools.modules.highwayborers.PosPosBorer;
import me.DNS.wmtools.modules.hud.TextPresets;
import me.DNS.wmtools.modules.main.AdvancedPlace;
import me.DNS.wmtools.modules.main.AfkLogout;
import me.DNS.wmtools.modules.main.AntiToS;
import me.DNS.wmtools.modules.main.AutoCenter;
import me.DNS.wmtools.modules.main.AutoDoors;
import me.DNS.wmtools.modules.main.AutoLogPlus;
import me.DNS.wmtools.modules.main.AutoWalkWM;
import me.DNS.wmtools.modules.main.AutoWalkP;
import me.DNS.wmtools.modules.main.AxisViewer;
import me.DNS.wmtools.modules.main.BedrockWalk;
import me.DNS.wmtools.modules.main.BoomView;
import me.DNS.wmtools.modules.main.Conditions;
import me.DNS.wmtools.modules.main.DiscordRPC;
import me.DNS.wmtools.modules.main.DoneLogout;
import me.DNS.wmtools.modules.main.Excuse;
import me.DNS.wmtools.modules.main.Gravity;
import me.DNS.wmtools.modules.main.Groupmessage;
import me.DNS.wmtools.modules.main.HighwayBuilderWM;
import me.DNS.wmtools.modules.main.HighwayTools;
import me.DNS.wmtools.modules.main.HitboxDesync;
import me.DNS.wmtools.modules.main.HotbarManager;
import me.DNS.wmtools.modules.main.Jitter;
import me.DNS.wmtools.modules.main.LiquidFillerWM;
import me.DNS.wmtools.modules.main.MassIgnore;
import me.DNS.wmtools.modules.main.NoPingDif;
import me.DNS.wmtools.modules.main.OffhandManager;
import me.DNS.wmtools.modules.main.PermJukebox;
import me.DNS.wmtools.modules.main.Pulse;
import me.DNS.wmtools.modules.main.RangeCMD;
import me.DNS.wmtools.modules.main.SafetyNet;
import me.DNS.wmtools.modules.main.ScaffoldWM;
import me.DNS.wmtools.modules.main.StopDrop;
import me.DNS.wmtools.modules.main.StreamerMode;
import me.DNS.wmtools.system.WMTab;
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

public class WMTools extends MeteorAddon {
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
        LogUtils.getLogger().info("Initializing Watchmen Additions {}", WMTools.VERSION);

        // Systems
        BetterChat.registerCustomHead("~Watchmen~", identifier("icon.png"));
        MeteorStarscript.ss.set("Watchmen", new ValueMap().set("version", VERSION));
        Tabs.add(new WMTab());

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
        modules.add(new AutoWalkWM());
        modules.add(new AxisViewer());
        modules.add(new DiscordRPC());
        modules.add(new HighwayBuilderWM());
        modules.add(new HighwayTools());
        modules.add(new HotbarManager());
        modules.add(new LiquidFillerWM());
        modules.add(new OffhandManager());
        modules.add(new ScaffoldWM());
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
        return "me.DNS.wmtools";
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
