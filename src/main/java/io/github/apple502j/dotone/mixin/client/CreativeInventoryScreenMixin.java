package io.github.apple502j.dotone.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.HotbarStorage;
import net.minecraft.client.option.HotbarStorageEntry;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.text.Text;

@Mixin(CreativeInventoryScreen.class)
public class CreativeInventoryScreenMixin {
    @Inject(method = "onHotbarKeyPress", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;setOverlayMessage(Lnet/minecraft/text/Text;Z)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void narrateHotbarSaved(MinecraftClient client, int index, boolean restore, boolean save, CallbackInfo ci, ClientPlayerEntity clientPlayerEntity, HotbarStorage hotbarStorage, HotbarStorageEntry hotbarStorageEntry, Text text, Text text2) {
        NarratorManager.INSTANCE.narrate(Text.translatable("inventory.hotbarSaved", text2, text));
    }
}
