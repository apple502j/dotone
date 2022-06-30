package io.github.apple502j.dotone.mixin;

import com.mojang.serialization.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.datafixer.fix.BlendingDataFix;

@Mixin(BlendingDataFix.class)
public class BlendingDataFixMixin {
    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    private static void checkDimension(Dynamic<?> dynamic, CallbackInfoReturnable<Dynamic<?>> cir) {
        if (!"minecraft:overworld".equals(dynamic.get("__context").get("dimension").asString().result().orElse(""))) {
            cir.setReturnValue(dynamic);
        }
    }
}
