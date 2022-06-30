package io.github.apple502j.dotone.mixin;

import java.time.Instant;

import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @Shadow public ServerPlayerEntity player;

    @Shadow @Final static Logger LOGGER;

    @Shadow private static boolean hasIllegalCharacter(String message) {
        return false;
    }

    @Shadow public abstract void disconnect(Text reason);

    @Shadow protected abstract boolean canAcceptMessage(String message, Instant timestamp);

    @Shadow @Final private MinecraftServer server;

    @Shadow protected abstract void checkForSpam();

    @Inject(method = "onRenameItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/c2s/play/RenameItemC2SPacket;getName()Ljava/lang/String;"), cancellable = true)
    private void checkRenameItem(RenameItemC2SPacket packet, CallbackInfo ci) {
        if (!this.player.currentScreenHandler.canUse(this.player)) {
            ci.cancel();
            LOGGER.debug("DotOne: invalid rename packet received");
        }
    }

    @Inject(method = "onUpdateBeacon", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/BeaconScreenHandler;setEffects(Ljava/util/Optional;Ljava/util/Optional;)V"), cancellable = true)
    private void checkBeacon(UpdateBeaconC2SPacket packet, CallbackInfo ci) {
        if (!this.player.currentScreenHandler.canUse(this.player)) {
            ci.cancel();
            LOGGER.debug("DotOne: invalid beacon packet received");
        }
    }

    @Inject(method = "onSelectMerchantTrade", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/MerchantScreenHandler;setRecipeIndex(I)V"), cancellable = true)
    private void checkMerchant(SelectMerchantTradeC2SPacket packet, CallbackInfo ci) {
        if (!this.player.currentScreenHandler.canUse(this.player)) {
            ci.cancel();
            LOGGER.debug("DotOne: invalid merchant packet received");
        }
    }

    @Inject(method = "onClickSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/ScreenHandler;isValid(I)Z"), cancellable = true)
    private void checkSlotClick(ClickSlotC2SPacket packet, CallbackInfo ci) {
        if (!this.player.currentScreenHandler.canUse(this.player)) {
            ci.cancel();
            LOGGER.debug("DotOne: invalid slot click packet received");
        }
    }

    @Inject(method = "onCraftRequest", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getRecipeManager()Lnet/minecraft/recipe/RecipeManager;"), cancellable = true)
    private void checkCraftRequest(CraftRequestC2SPacket packet, CallbackInfo ci) {
        if (!this.player.currentScreenHandler.canUse(this.player)) {
            ci.cancel();
            LOGGER.debug("DotOne: invalid crafting packet received");
        }
    }

    @Inject(method = "onButtonClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/ScreenHandler;onButtonClick(Lnet/minecraft/entity/player/PlayerEntity;I)Z"), cancellable = true)
    private void checkButtonClick(ButtonClickC2SPacket packet, CallbackInfo ci) {
        if (!this.player.currentScreenHandler.canUse(this.player)) {
            ci.cancel();
            LOGGER.debug("DotOne: invalid button click packet received");
        }
    }

    @Inject(method = "onCommandExecution", at = @At("HEAD"), cancellable = true)
    private void replaceCommandHandling(CommandExecutionC2SPacket packet, CallbackInfo ci) {
        // @Overwrite or @Inject? idk
        ci.cancel();
        if (hasIllegalCharacter(packet.command())) {
            this.disconnect(Text.translatable("multiplayer.disconnect.illegal_characters"));
            return;
        }
        if (this.canAcceptMessage(packet.command(), packet.timestamp())) {
            this.server.submit(() -> {
                ServerCommandSource commandSource = this.player.getCommandSource().withSigner(packet.createArgumentsSigner(this.player.getUuid()));
                this.server.getCommandManager().execute(commandSource, packet.command());
                this.checkForSpam();
            });
        }
    }
}
