package essentialaddons;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import com.mojang.brigadier.CommandDispatcher;
import essentialaddons.commands.*;
import essentialaddons.helpers.CameraData;
import essentialaddons.helpers.ReloadFakePlayers;
import essentialaddons.helpers.SubscribeData;
import essentialaddons.logging.EssentialAddonsLoggerRegistry;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class EssentialAddonsServer implements CarpetExtension, ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger("EssentialAddons");

    @Override
    public String version() { return "essentialaddons"; }

    @Override
    public void onInitialize() { CarpetServer.manageExtension(new EssentialAddonsServer()); }

    @Override
    public void onGameStarted() { CarpetServer.settingsManager.parseSettingsClass(EssentialAddonsSettings.class); }

    @Override
    public void onServerLoaded(MinecraftServer server) {
        CarpetExtension.super.onServerLoaded(server);
        String dataResult;
        try {
            CameraData.cameraData = CameraData.readSaveFile();
            dataResult = "Successfully read camera data file";
        }
        catch (IOException e) {
            dataResult = "Failed to read camera data file";
        }
        if (EssentialAddonsSettings.commandCameraMode && EssentialAddonsSettings.cameraModeRestoreLocation)
            LOGGER.info(dataResult);
        try {
            SubscribeData.subscribeData = SubscribeData.readSaveFile();
            dataResult = "Successfully read subscribe data file";
        }
        catch (IOException e) {
            dataResult = "Failed to read subscribe data file";
        }
        if (EssentialAddonsSettings.essentialCarefulBreak)
            LOGGER.info(dataResult);
    }

    @Override
    public void registerLoggers() {
        EssentialAddonsLoggerRegistry.registerLoggers();
    }

    @Override
    public void onServerLoadedWorlds(MinecraftServer server) {
        CarpetExtension.super.onServerLoadedWorlds(server);
        if (EssentialAddonsSettings.reloadFakePlayers)
            ReloadFakePlayers.loadFakePlayers(server);
    }

    @Override
    public void onServerClosed(MinecraftServer server) {
        CarpetExtension.super.onServerClosed(server);
        boolean didSave;
        try {
            didSave = CameraData.writeSaveFile(CameraData.cameraData);
        }
        catch (IOException e) {
            didSave = false;
            e.printStackTrace();
            LOGGER.error("Failed to write camera data file ");
        }
        if (EssentialAddonsSettings.commandCameraMode && EssentialAddonsSettings.cameraModeRestoreLocation && didSave)
            LOGGER.info("Successfully wrote to camera data file ");
        if (EssentialAddonsSettings.reloadFakePlayers)
            ReloadFakePlayers.saveFakePlayers(CarpetServer.minecraft_server);
    }

    @Override
    public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        CommandRegion.register(dispatcher);
        CommandFly.register(dispatcher);
        CommandHat.register(dispatcher);
        CommandRepair.register(dispatcher);
        CommandGM.register(dispatcher);
        CommandHeal.register(dispatcher);
        CommandExtinguish.register(dispatcher);
        CommandGod.register(dispatcher);
        CommandDefuse.register(dispatcher);
        CommandMore.register(dispatcher);
        CommandStrength.register(dispatcher);
        CommandNightVision.register(dispatcher);
        CommandDimensions.register(dispatcher);
        CommandWarp.register(dispatcher);
        CommandCameraMode.register(dispatcher);
        CommandSwitchDimensions.register(dispatcher);
        CommandEnderChest.register(dispatcher);
        CommandWorkbench.register(dispatcher);
        CommandPublicViewDistance.register(dispatcher);
        CommandSubscribe.register(dispatcher);
        CommandTop.register(dispatcher);
        CommandNear.register(dispatcher);
        CommandLagSpike.register(dispatcher);
        CommandRename.register(dispatcher);
        CommandMods.register(dispatcher);
        CommandPlayerFake.register(dispatcher);
    }
}
