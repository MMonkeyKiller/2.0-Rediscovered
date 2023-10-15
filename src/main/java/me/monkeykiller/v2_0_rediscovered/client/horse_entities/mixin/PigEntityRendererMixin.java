package me.monkeykiller.v2_0_rediscovered.client.horse_entities.mixin;

import me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered;
import me.monkeykiller.v2_0_rediscovered.common.horse_entities.HorseEntityAccessor;
import net.minecraft.client.render.entity.PigEntityRenderer;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PigEntityRenderer.class)
public abstract class PigEntityRendererMixin {
    @Inject(method = "getTexture(Lnet/minecraft/entity/passive/PigEntity;)Lnet/minecraft/util/Identifier;", at = @At("RETURN"), cancellable = true)
    public void getTexture(PigEntity pigEntity, CallbackInfoReturnable<Identifier> cir) {
        if (pigEntity instanceof HorseEntityAccessor horseEntityAccessor && horseEntityAccessor.isHorse()) {
            cir.setReturnValue(V2_0_Rediscovered.identifier("textures/entity/pig/pig_horse.png"));
        }
    }
}
