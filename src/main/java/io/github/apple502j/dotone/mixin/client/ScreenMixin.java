package io.github.apple502j.dotone.mixin.client;

import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.SharedConstants;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.ClickEvent;

@Mixin(Screen.class)
public class ScreenMixin {
    @Shadow @Final private static Logger LOGGER;

    @Redirect(method = "handleTextClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/ClickEvent;getValue()Ljava/lang/String;", ordinal = 5))
    private String stripInvalid(ClickEvent event) {
        return SharedConstants.stripInvalidChars(event.getValue());
    }

    @Redirect(method = "handleTextClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/ClickEvent;getValue()Ljava/lang/String;", ordinal = 6))
    private String stripInvalid2(ClickEvent event) {
        return SharedConstants.stripInvalidChars(event.getValue());
    }

    @Redirect(method = "handleTextClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;sendChatMessage(Ljava/lang/String;)V"))
    private void cancelChatSubmission(ClientPlayerEntity instance, String message) {
        LOGGER.warn("DotOne: tried to send unprefixed command/chat message {}", message);
    }
}
