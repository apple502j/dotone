package io.github.apple502j.dotone.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.brain.task.LayFrogSpawnTask;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

@Mixin(LayFrogSpawnTask.class)
public class LayFrogSpawnTaskMixin {
    @Redirect(method = "run(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/FrogEntity;J)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;", ordinal = 0))
    private BlockState replaceBlockStateCheck(ServerWorld world, BlockPos pos) {
        if (world.getBlockState(pos).getCollisionShape(world, pos).getFace(Direction.UP).isEmpty()) {
            if (world.getFluidState(pos).isOf(Fluids.WATER)) {
                return Blocks.WATER.getDefaultState();
            }
        }

        return Blocks.AIR.getDefaultState();
    }
}
