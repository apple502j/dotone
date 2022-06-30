package io.github.apple502j.dotone.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ProjectileAttackGoal;

@Mixin(ProjectileAttackGoal.class)
public abstract class ProjectileAttackGoalMixin {
    @Shadow @Nullable private LivingEntity target;

    @Shadow public abstract boolean canStart();

    @Inject(method = "shouldContinue", at = @At("HEAD"), cancellable = true)
    private void checkIfDead(CallbackInfoReturnable<Boolean> cir) {
        if (this.target != null && !this.target.isAlive()) cir.setReturnValue(this.canStart());
    }
}
