package io.github.apple502j.dotone.mixin;

import java.util.Set;

import com.mojang.datafixers.util.Pair;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.placement.ConcentricRingsStructurePlacement;
import net.minecraft.world.gen.structure.Structure;

@Mixin(ChunkGenerator.class)
public abstract class ChunkGeneratorMixin {
    @Shadow @Nullable protected abstract Pair<BlockPos, RegistryEntry<Structure>> locateConcentricRingsStructure(Set<RegistryEntry<Structure>> structures, ServerWorld world, StructureAccessor structureAccessor, BlockPos center, boolean skipReferencedStructures, ConcentricRingsStructurePlacement placement);

    @Redirect(method = "locateStructure(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/registry/RegistryEntryList;Lnet/minecraft/util/math/BlockPos;IZ)Lcom/mojang/datafixers/util/Pair;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/chunk/ChunkGenerator;locateConcentricRingsStructure(Ljava/util/Set;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/util/math/BlockPos;ZLnet/minecraft/world/gen/chunk/placement/ConcentricRingsStructurePlacement;)Lcom/mojang/datafixers/util/Pair;"))
    private Pair<BlockPos, RegistryEntry<Structure>> locateStructureHack(ChunkGenerator chunkGenerator, Set<RegistryEntry<Structure>> structures, ServerWorld world, StructureAccessor structureAccessor, BlockPos center, boolean skipReferencedStructures, ConcentricRingsStructurePlacement placement) {
        Pair<BlockPos, RegistryEntry<Structure>> original = this.locateConcentricRingsStructure(structures, world, structureAccessor, center, skipReferencedStructures, placement);
        if (original != null) return original;
        // We can't return null here because it NPEs
        // instead return a bogus entry
        // While the first null can NPE in vanilla, we patch it again
        return Pair.of(null, null);
    }

    @Redirect(method = "locateStructure(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/registry/RegistryEntryList;Lnet/minecraft/util/math/BlockPos;IZ)Lcom/mojang/datafixers/util/Pair;", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/BlockPos;getSquaredDistance(Lnet/minecraft/util/math/Vec3i;)D", ordinal = 0))
    private double skipNullPos(BlockPos pos, Vec3i pos2) {
        if (pos2 == null) return Double.MAX_VALUE;
        return pos.getSquaredDistance(pos2);
    }
}
