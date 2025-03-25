package me.monkeykiller.v2_0_rediscovered.common.furnace_heat.mixin;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

import static me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered.CONFIG_COMMON;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin {
    @Shadow
    protected abstract boolean isBurning();

    @Accessor
    abstract int getBurnTime();

    @Accessor
    abstract void setBurnTime(int value);

    private int heat;

    @Inject(at = @At("TAIL"), method = "readNbt")
    private void readHeatNBT(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup, CallbackInfo ci) {
        if (CONFIG_COMMON.furnace_heat.enabled) {
            this.heat = nbt.getInt("Heat");
        }
    }

    @Inject(at = @At("TAIL"), method = "writeNbt")
    private void writeHeatNBT(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup, CallbackInfo ci) {
        if (CONFIG_COMMON.furnace_heat.enabled) {
            nbt.putInt("Heat", this.heat);
        }
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/AbstractFurnaceBlockEntity;isBurning()Z", ordinal = 1), method = "tick")
    private static boolean decreaseBurnTime(@NotNull AbstractFurnaceBlockEntity instance) {
        var blockEntity = (AbstractFurnaceBlockEntityMixin) (Object) instance;

        if (!CONFIG_COMMON.furnace_heat.enabled) {
            return blockEntity.isBurning();
        }

        var random = new Random();
        if (blockEntity.isBurning()) {
            blockEntity.setBurnTime(blockEntity.getBurnTime() - 1);

            if (random.nextFloat() < 0.5F) {
                blockEntity.heat++;
            }
        } else if (random.nextFloat() < 0.25f) {
            blockEntity.heat--;
        }

        float stage = (float) blockEntity.heat / 700.0F;
        blockEntity.heat = Math.max(0, Math.min(700, blockEntity.heat));

        var pos = instance.getPos().toCenterPos();
        if (instance.getWorld() instanceof ServerWorld world) {
            if (random.nextFloat() < stage) {
                world.spawnParticles(ParticleTypes.LARGE_SMOKE.getType(), pos.getX(), pos.getY(), pos.getZ(), 3, .5, .5, .5, 0.019999999552965164D);
            }

            if (stage >= 1.0F && random.nextFloat() < 0.25F) {
                world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 5.0F, true, World.ExplosionSourceType.BLOCK);
            }
        }

        return false;
    }


}
