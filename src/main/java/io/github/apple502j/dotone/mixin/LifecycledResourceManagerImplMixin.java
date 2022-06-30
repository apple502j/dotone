package io.github.apple502j.dotone.mixin;

import java.util.List;
import java.util.stream.Stream;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.resource.LifecycledResourceManagerImpl;

@Mixin(LifecycledResourceManagerImpl.class)
public class LifecycledResourceManagerImplMixin {
    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;toList()Ljava/util/List;", ordinal = 0))
    private <T> List<T> distinctList(Stream<T> stream) {
        return stream.distinct().toList();
    }
}
