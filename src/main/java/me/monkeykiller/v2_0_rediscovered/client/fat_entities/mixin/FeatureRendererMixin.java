package me.monkeykiller.v2_0_rediscovered.client.fat_entities.mixin;

import me.monkeykiller.v2_0_rediscovered.client.fat_entities.AnimalModelAccessor;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FeatureRenderer.class)
public abstract class FeatureRendererMixin {
    @Inject(at = @At("HEAD"), method = "renderModel", cancellable = true)
    private static <T extends LivingEntity> void renderModel(EntityModel<T> model, Identifier texture, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float red, float green, float blue, CallbackInfo ci) {
        ci.cancel();
        var vertices = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(texture));
        var overlay = LivingEntityRenderer.getOverlay(entity, 0.0f);
        var alpha = 1.0f;

        if (model instanceof AnimalModelAccessor animalModel) {
            animalModel.render(entity, matrices, vertices, light, overlay, red, green, blue, alpha);
            return;
        }
        model.render(matrices, vertices, light, overlay, red, green, blue, alpha);
    }
}
