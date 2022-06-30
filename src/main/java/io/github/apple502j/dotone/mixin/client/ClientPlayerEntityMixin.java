package io.github.apple502j.dotone.mixin.client;

import java.time.Instant;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.message.ArgumentSignatureDataMap;
import net.minecraft.network.packet.c2s.play.CommandExecutionC2SPacket;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Shadow @Final public ClientPlayNetworkHandler networkHandler;

    @Inject(method = "sendCommand(Ljava/lang/String;)V", at = @At("HEAD"), cancellable = true)
    private void sendCommand(String command, CallbackInfo ci) {
        ci.cancel();
        this.networkHandler.sendPacket(new CommandExecutionC2SPacket(command, Instant.now(), ArgumentSignatureDataMap.empty(), false));
    }
}
