package me.monkeykiller.v2_0_rediscovered.common.horse_entities.mixin;

import me.monkeykiller.v2_0_rediscovered.common.horse_entities.HorseEntityAccessor;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin implements HorseEntityAccessor {
    @Inject(at = @At("TAIL"), method = "initDataTracker")
    protected void injectDataTracker(CallbackInfo ci) {
        var self = (MobEntity) (Object) this;
        if (self instanceof CowEntity || self instanceof PigEntity) {
            setupDataTracker();
        }
    }

    @Inject(at = @At("TAIL"), method = "initialize")
    public void initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cir) {
        var self = (MobEntity) (Object) this;
        if (self instanceof CowEntity || self instanceof PigEntity) {
            setupHorse();
        }
    }

    @Inject(at = @At("TAIL"), method = "readCustomDataFromNbt")
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        var self = ((MobEntity) (Object) this);
        if (self instanceof CowEntity || self instanceof PigEntity) {
            setHorse(nbt.getBoolean("IsHorse"));
        }
    }

    @Inject(at = @At("TAIL"), method = "writeCustomDataToNbt")
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        var self = ((MobEntity) (Object) this);
        if (self instanceof CowEntity || self instanceof PigEntity) {
            nbt.putBoolean("IsHorse", isHorse());
        }
    }
}
