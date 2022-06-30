package io.github.apple502j.dotone.mixin.client;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.gui.hud.DebugHud;

@Mixin(DebugHud.class)
public class DebugHudMixin {
    @Redirect(method = "getLeftText", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 13))
    private <E> boolean skipNewBlending(List<E> lines, E line) {
        if ("Blending: Old".equals(line)) {
            return lines.add(line);
        }

        return false;
    }
}
