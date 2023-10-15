package me.monkeykiller.v2_0_rediscovered.client.horse_entities.mixin;

import me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered;
import me.monkeykiller.v2_0_rediscovered.common.horse_entities.HorseEntityAccessor;
import net.minecraft.client.render.entity.CowEntityRenderer;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CowEntityRenderer.class)
public abstract class CowEntityRendererMixin {
    @Inject(method = "getTexture(Lnet/minecraft/entity/passive/CowEntity;)Lnet/minecraft/util/Identifier;", at = @At("RETURN"), cancellable = true)
    public void getTexture(CowEntity cowEntity, CallbackInfoReturnable<Identifier> cir) {
        if (cowEntity instanceof HorseEntityAccessor horseEntityAccessor && horseEntityAccessor.isHorse()) {
            cir.setReturnValue(V2_0_Rediscovered.identifier("textures/entity/cow/cow_horse.png"));
        }
    }
}
