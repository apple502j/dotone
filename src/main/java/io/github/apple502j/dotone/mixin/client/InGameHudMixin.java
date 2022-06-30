package io.github.apple502j.dotone.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I", ordinal = 0))
    private int drawActionBarWithShadow(TextRenderer textRenderer, MatrixStack matrices, Text text, float x, float y, int color) {
        return textRenderer.drawWithShadow(matrices, text, x, y, color);
    }

    @Inject(method = "setRecordPlayingOverlay", at = @At("TAIL"))
    private void narrateRecord(Text description, CallbackInfo ci) {
        NarratorManager.INSTANCE.narrate(Text.translatable("record.nowPlaying", description));
    }
}
