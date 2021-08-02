package essentialaddons.commands;

import carpet.settings.SettingsManager;
import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.EssentialAddonsSettings;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

import java.util.Collection;
import java.util.LinkedList;

import static net.minecraft.server.command.CommandManager.literal;

public class CommandMods {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("mods").requires((player) -> SettingsManager.canUseCommand(player, EssentialAddonsSettings.commandMods))
                .executes(context -> {
                    ServerPlayerEntity playerEntity = context.getSource().getPlayer();
                    Collection<ModContainer> mods = FabricLoader.getInstance().getAllMods();
                    Collection<ModContainer> usefulMods = new LinkedList<>();
                    for (ModContainer mod : mods) {
                        if (!mod.getMetadata().getType().equals("builtin") && !mod.getMetadata().getId().equalsIgnoreCase("fabricloader") && !mod.getMetadata().containsCustomValue("fabric-api:module-lifecycle"))
                            usefulMods.add(mod);
                    }
                    String[] modNames = new String[usefulMods.size()];
                    int i = 0;
                    for (ModContainer mod : usefulMods) {
                        String modName = mod.getMetadata().getName();
                        modNames[i] = modName;
                        i++;
                    }
                    if (modNames.length == 0)
                        playerEntity.sendMessage(new LiteralText("There are no mods installed"), false);
                    else
                        playerEntity.sendMessage(new LiteralText("§6Installed Mods: §a" + String.join(", ", modNames)), false);
                    return 0;
                })
        );
    }
}
