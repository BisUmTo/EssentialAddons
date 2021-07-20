package essentialaddons.mixins;

import carpet.CarpetServer;
import essentialaddons.EssentialAddonsSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {

    @Shadow @Final private static Logger LOGGER;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        if (EssentialAddonsSettings.removeXpEntitiesIfMsptOver > 0) {
            double mspt = MathHelper.average(CarpetServer.minecraft_server.lastTickLengths) * 1.0E-6D;
            if (mspt > EssentialAddonsSettings.removeXpEntitiesIfMsptOver) {
                ServerCommandSource source = CarpetServer.minecraft_server.getCommandSource();
                //CarpetServer.minecraft_server.getCommandManager().execute(source, "kill @e[type=experience_orb]");
                List<Entity> overworld = Objects.requireNonNull(CarpetServer.minecraft_server.getWorld(World.OVERWORLD)).getEntitiesByType(EntityType.EXPERIENCE_ORB, ExperienceOrbEntity -> true);
                List<Entity> nether = Objects.requireNonNull(CarpetServer.minecraft_server.getWorld(World.NETHER)).getEntitiesByType(EntityType.EXPERIENCE_ORB, ExperienceOrbEntity -> true);
                List<Entity> end = Objects.requireNonNull(CarpetServer.minecraft_server.getWorld(World.END)).getEntitiesByType(EntityType.EXPERIENCE_ORB, ExperienceOrbEntity -> true);
                if (!overworld.isEmpty())
                    overworld.iterator().next().remove();
                if (!nether.isEmpty())
                    nether.iterator().next().remove();
                if (!end.isEmpty())
                    end.iterator().next().remove();
            }
        }
    }
}