package me.monkeykiller.v2_0_rediscovered.common.redstone_bug.mixin;

import me.monkeykiller.v2_0_rediscovered.common.redstone_bug.RedstoneBugAccessor;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered.CONFIG_COMMON;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin implements RedstoneBugAccessor {
    @Inject(at = @At("TAIL"), method = "initDataTracker")
    protected void injectDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
        var self = (MobEntity) (Object) this;
        if (self instanceof SilverfishEntity) {
            setupDataTracker(builder);
        }
    }

    @Inject(at = @At("TAIL"), method = "readCustomDataFromNbt")
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if (!CONFIG_COMMON.redstone_bugs.enabled) return;
        var self = (MobEntity) (Object) this;
        if (self instanceof SilverfishEntity) {
            setColor(nbt.getInt("Color"));
        }
    }

    @Inject(at = @At("TAIL"), method = "writeCustomDataToNbt")
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        var self = (MobEntity) (Object) this;
        if (self instanceof SilverfishEntity) {
            nbt.putInt("Color", getColor());
        }
    }
}
