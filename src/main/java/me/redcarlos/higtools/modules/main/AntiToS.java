package me.DNS.vmtools.modules.main;

import java.io.File;
import java.util.HashSet;
import java.nio.file.Files;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import me.DNS.higtools.WMTools;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import me.DNS.vmtools.utils.StardustUtil;
import net.minecraft.block.entity.SignText;
import net.fabricmc.loader.api.FabricLoader;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;

/**
 * @author Tas [0xTas] <root@0xTas.dev>
 **/
public class AntiToS extends Module {
    public AntiToS() { super(HIGTools.MAIN, "Censor", "Censor player-generated text sources according to a content blacklist."); }

    private final String BLACKLIST_FILE = "meteor-client/anti-tos.txt";

    private final SettingGroup sgSources = settings.createGroup("Source Settings");
    private final SettingGroup sgBlacklist = settings.createGroup("Content Settings");

    public enum SignMode {
        Censor, Replace
    }

    public enum ChatMode {
        Censor, Remove
    }

    public final Setting<ChatMode> chatMode = sgSources.add(
        new EnumSetting.Builder<ChatMode>()
            .name("chat-mode")
            .description("Censor or completely replace text in chat that matches the filter.")
            .defaultValue(ChatMode.Censor)
            .build()
    );
    private final Setting<SignMode> signMode = sgSources.add(
        new EnumSetting.Builder<SignMode>()
            .name("sign-mode")
            .description("Censor or completely replace SignText that matches the filter.")
            .defaultValue(SignMode.Censor)
            .build()
    );
    private final Setting<String> familyFriendlyLine1 = sgSources.add(
        new StringSetting.Builder()
            .name("replacement-line-1")
            .defaultValue("Original text")
            .visible(() -> signMode.get() == SignMode.Replace)
            .build()
    );
    private final Setting<String> familyFriendlyLine2 = sgSources.add(
        new StringSetting.Builder()
            .name("replacement-line-2")
            .defaultValue("was replaced by")
            .visible(() -> signMode.get() == SignMode.Replace)
            .build()
    );
    private final Setting<String> familyFriendlyLine3 = sgSources.add(
        new StringSetting.Builder()
            .name("replacement-line-3")
            .defaultValue("Stardust AntiToS")
            .visible(() -> signMode.get() == SignMode.Replace)
            .build()
    );
    private final Setting<String> familyFriendlyLine4 = sgSources.add(
        new StringSetting.Builder()
            .name("replacement-line-4")
            .defaultValue("plz no ban ☺")
            .visible(() -> signMode.get() == SignMode.Replace)
            .build()
    );
    private final Setting<DyeColor> familyFriendlyColor = sgSources.add(
        new EnumSetting.Builder<DyeColor>()
            .name("replacement-color")
            .description("Render replacement SignText with the selected dye color.")
            .defaultValue(DyeColor.RED)
            .visible(() -> signMode.get() == SignMode.Replace)
            .build()
    );
    private final Setting<Boolean> familyFriendlyGlowing = sgSources.add(
        new BoolSetting.Builder()
            .name("replacement-glowing")
            .description("Render replacement SignText with glowing text.")
            .defaultValue(true)
            .visible(() -> signMode.get() == SignMode.Replace)
            .build()
    );

    private final Setting<Boolean> openBlacklistFile = sgBlacklist.add(
        new BoolSetting.Builder()
            .name("open-blacklist-file")
            .description("Open the anti-tos.txt file.")
            .defaultValue(false)
            .onChanged(it -> {
                if (it) {
                    if (StardustUtil.checkOrCreateFile(mc, BLACKLIST_FILE)) StardustUtil.openFile(mc, BLACKLIST_FILE);
                    resetBlacklistFileSetting();
                }
            })
            .build()
    );

    private final HashSet<String> blacklisted = new HashSet<>();

    private void resetBlacklistFileSetting() { openBlacklistFile.set(false); }

    private void initBlacklistText() {
        File blackListFile = FabricLoader.getInstance().getGameDir().resolve(BLACKLIST_FILE).toFile();

        try(Stream<String> lineStream = Files.lines(blackListFile.toPath())) {
            blacklisted.addAll(lineStream.toList());
            if (blacklisted.isEmpty()) {
                mc.player.sendMessage(Text.of("meteor-client/anti-tos.txt Is empty!"), true);
                mc.player.sendMessage(Text.of("Please write one blacklisted item for each line of the file."), true);
                mc.player.sendMessage(Text.of("Spaces and other punctuation will be treated literally"), true);
                mc.player.sendMessage(Text.of("You must toggle this setting or the module after updating the blacklist's contents."), true);
            }
        }catch (Exception err) {}
    }

    // See ChatHudMixin.java
    // && ItemStackMixin.java
    // && InGameHudMixin.java
    // && BookScreenMixin.java
    // && TextRendererMixin.java
    // && EntityRendererMixin.java
    // && SignBlockEntityRendererMixin.java
    public boolean containsBlacklistedText(String text) {
        return blacklisted.stream().anyMatch(line -> text.trim().toLowerCase().contains(line.trim().toLowerCase()));
    }

    public String censorText(String text) {
        for (String filter : blacklisted) {
            text = text.replaceAll("(?i)"+ Pattern.quote(filter), "*".repeat(filter.length()));
        }
        return text;
    }

    public SignText familyFriendlySignText(SignText original) {
        if (signMode.get() == SignMode.Censor) {
            Text[] lines = {
                Text.of(censorText(original.getMessage(0, false).getString())),
                Text.of(censorText(original.getMessage(1, false).getString())),
                Text.of(censorText(original.getMessage(2, false).getString())),
                Text.of(censorText(original.getMessage(3, false).getString()))
            };
            return new SignText(lines, lines, original.getColor(), original.isGlowing());
        } else {
            Text[] lines = {
                Text.of(familyFriendlyLine1.get()),
                Text.of(familyFriendlyLine2.get()),
                Text.of(familyFriendlyLine3.get()),
                Text.of(familyFriendlyLine4.get())
            };
            return new SignText(lines, lines, familyFriendlyColor.get(), familyFriendlyGlowing.get());
        }
    }

    @Override
    public void onActivate() {
        if (StardustUtil.checkOrCreateFile(mc, BLACKLIST_FILE)) initBlacklistText();
        else {
            toggle();
        }
    }

    @Override
    public void onDeactivate() { blacklisted.clear(); }
}
