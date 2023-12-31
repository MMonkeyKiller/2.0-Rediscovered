package me.monkeykiller.v2_0_rediscovered.common.fat_entities.mixin;

import me.monkeykiller.v2_0_rediscovered.common.fat_entities.FatEntityAccessor;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements FatEntityAccessor {
    @Inject(at = @At("RETURN"), method = "getSoundPitch", cancellable = true)
    public void getSoundPitch(CallbackInfoReturnable<Float> cir) {
        var self = (LivingEntity) (Object) this;
        if (isFattenable() && !self.isBaby()) {
            cir.setReturnValue(cir.getReturnValue() - 0.05F * (float) getFatness());
        }
    }
}
