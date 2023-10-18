package me.monkeykiller.v2_0_rediscovered.client.fat_entities.mixin;

import me.monkeykiller.v2_0_rediscovered.client.fat_entities.AnimalModelAccessor;
import me.monkeykiller.v2_0_rediscovered.common.fat_entities.FatEntityAccessor;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.PigEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;

@Mixin(AnimalModel.class)
public abstract class AnimalModelMixin implements AnimalModelAccessor {
    @Invoker("getHeadParts")
    public abstract Iterable<ModelPart> getHeadParts();

    @Invoker("getBodyParts")
    public abstract Iterable<ModelPart> getBodyParts();

    @Override
    public void render(Entity entity, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        var model = (AnimalModel<?>) (Object) this;
        if (model.child || !(entity instanceof FatEntityAccessor fatEntity) || entity instanceof ChickenEntity || !fatEntity.isFattenable()) {
            model.render(matrices, vertices, light, overlay, red, green, blue, alpha);
            return;
        }

        var fatness = fatEntity.getFatness();
        var blockScale = 1 / 16f;

        // Push a new matrix for the head
        matrices.push();
        matrices.translate(0.0F, -((float) fatness) / 2.0F * blockScale, -((float) fatness) / 2.0F * blockScale);
        getHeadParts().forEach((headPart) -> {
            headPart.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        });
        matrices.pop();
        // --

        // Push a new matrix for the body part
        matrices.push();
        var bodyScale = 1.0F + fatness * blockScale;

        var bodyOffsetMultiplier = 0.5F;
        if (fatEntity instanceof PigEntity) {
            bodyOffsetMultiplier = 0.75F;
        }
        matrices.translate(0.0F, -bodyOffsetMultiplier * (float) fatness * blockScale, 0.0F);
        matrices.scale(bodyScale, bodyScale, bodyScale);

        var bodyParts = new ArrayList<ModelPart>();
        getBodyParts().forEach(bodyParts::add);

        bodyParts.get(0).render(matrices, vertices, light, overlay, red, green, blue, alpha);
        matrices.pop();
        // --

        matrices.push(); // Push a new matrix for the rest of the body parts
        // Render the rest of the body parts without scaling
        for (int i = 1; i < bodyParts.size(); i++) {
            bodyParts.get(i).render(matrices, vertices, light, overlay, red, green, blue, alpha);
        }
        matrices.pop(); // Pop the main matrix
    }
}
