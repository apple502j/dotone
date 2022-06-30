package io.github.apple502j.dotone.mixin.client;

import java.io.File;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.ClientBuiltinResourcePackProvider;

@Mixin(ClientBuiltinResourcePackProvider.class)
public abstract class ClientBuiltinResourcePackProviderMixin {
    @Shadow protected abstract void deleteOldServerPack();

    @Redirect(method = "download", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resource/ClientBuiltinResourcePackProvider;clear()Ljava/util/concurrent/CompletableFuture;", ordinal = 0))
    private CompletableFuture<?> dontClear(ClientBuiltinResourcePackProvider provider) {
        return CompletableFuture.completedFuture(null);
    }

    @Redirect(method = "download", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resource/ClientBuiltinResourcePackProvider;deleteOldServerPack()V", ordinal = 0))
    private void dontDelete(ClientBuiltinResourcePackProvider provider) {
    }

    @Inject(method = "method_19436", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resource/ClientBuiltinResourcePackProvider;delete(Ljava/io/File;)V"))
    private static void clearHere(File file, Void void_, Throwable throwable, CallbackInfo ci) {
        MinecraftClient.getInstance().getResourcePackProvider().clear();
    }

    @Inject(method = "download", at = @At(value = "INVOKE", target = "Ljava/util/concurrent/CompletableFuture;whenComplete(Ljava/util/function/BiConsumer;)Ljava/util/concurrent/CompletableFuture;"))
    private void deleteHere(URL url, String packSha1, boolean closeAfterDownload, CallbackInfoReturnable<CompletableFuture<?>> cir) {
        this.deleteOldServerPack();
    }
}
