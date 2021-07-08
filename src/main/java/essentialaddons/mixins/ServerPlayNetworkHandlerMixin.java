package essentialaddons.mixins;

import essentialaddons.EssentialAddonsSettings;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
class ServerPlayNetHandlerMixin {

    @Inject(method="onClickSlot", at=@At("HEAD"))
    public void onClickSlotStarts(ClickSlotC2SPacket packet, CallbackInfo ci){
        EssentialAddonsSettings.inventoryStacking = true;
    }

    @Inject(method="onClickSlot", at=@At("RETURN"))
    public void onClickSlotEnds(ClickSlotC2SPacket packet, CallbackInfo ci){
        EssentialAddonsSettings.inventoryStacking = false;
    }
}