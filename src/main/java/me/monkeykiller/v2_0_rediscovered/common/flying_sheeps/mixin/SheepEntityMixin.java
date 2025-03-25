package me.monkeykiller.v2_0_rediscovered.common.flying_sheeps.mixin;

import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered.CONFIG_COMMON;

@Mixin(SheepEntity.class)
public abstract class SheepEntityMixin {
    @Unique
    private static final TrackedData<Boolean> FLOATING = DataTracker.registerData(SheepEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Inject(at = @At("TAIL"), method = "initDataTracker")
    protected void injectDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(FLOATING, false);
    }

    @Inject(at = @At("TAIL"), method = "readCustomDataFromNbt")
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if (CONFIG_COMMON.flying_sheeps.enabled) {
            setFloating(nbt.getBoolean("Floating"));
        }
    }

    @Inject(at = @At("TAIL"), method = "writeCustomDataToNbt")
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        if (CONFIG_COMMON.flying_sheeps.enabled) {
            nbt.putBoolean("Floating", isFloating());
        }
    }

    @Unique
    public boolean isFloating() {
        var self = (SheepEntity) (Object) this;
        return self.getDataTracker().get(FLOATING);
    }

    @Unique
    public void setFloating(boolean value) {
        var self = (SheepEntity) (Object) this;
        self.getDataTracker().set(FLOATING, value);
    }

    @Inject(at = @At("HEAD"), method = "mobTick")
    public void mobTick(CallbackInfo ci) {
        var self = (SheepEntity) (Object) this;
        if (CONFIG_COMMON.flying_sheeps.enabled && isFloating()) {
            self.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 1, 1, false, false, false));
        }
    }

    @Inject(at = @At("TAIL"), method = "interactMob", cancellable = true)
    public void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (!CONFIG_COMMON.flying_sheeps.enabled) return;
        var self = (SheepEntity) (Object) this;
        var itemStack = player.getStackInHand(hand);
        if ((itemStack.isOf(Items.GOLDEN_APPLE) || itemStack.isOf(Items.ENCHANTED_GOLDEN_APPLE) && !isFloating())) {
            if (!self.getWorld().isClient()) {
                setFloating(true);
            }
            itemStack.decrement(1);
            self.playAmbientSound();
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }
}

