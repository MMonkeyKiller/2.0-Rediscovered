package me.monkeykiller.v2_0_rediscovered.client.fat_entities;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public interface AnimalModelAccessor {
    Iterable<ModelPart> getHeadParts();

    Iterable<ModelPart> getBodyParts();

    void render(Entity entity, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color);
}
