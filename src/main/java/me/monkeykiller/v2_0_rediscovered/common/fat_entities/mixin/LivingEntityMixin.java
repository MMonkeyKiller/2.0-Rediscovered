package me.monkeykiller.v2_0_rediscovered.common.fat_entities.mixin;

import me.monkeykiller.v2_0_rediscovered.common.fat_entities.FatEntityAccessor;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered.CONFIG_COMMON;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements FatEntityAccessor {
    @Inject(at = @At("RETURN"), method = "getSoundPitch", cancellable = true)
    public void getSoundPitch(CallbackInfoReturnable<Float> cir) {
        if (!CONFIG_COMMON.mob_fatness.enabled) return;
        var self = (LivingEntity) (Object) this;
        if (isFattenable() && !self.isBaby()) {
            cir.setReturnValue(cir.getReturnValue() - 0.05F * (float) getFatness());
        }
    }
}
