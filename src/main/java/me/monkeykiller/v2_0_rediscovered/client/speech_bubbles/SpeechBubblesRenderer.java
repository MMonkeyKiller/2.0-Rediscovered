package me.monkeykiller.v2_0_rediscovered.client.speech_bubbles;

import me.monkeykiller.v2_0_rediscovered.common.speech_bubbles.SpeechBubbleEntity;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;

public class SpeechBubblesRenderer extends EntityRenderer<SpeechBubbleEntity> {
    public SpeechBubblesRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(SpeechBubbleEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        var textRenderer = getTextRenderer();
        var scaleFactor = 0.025F;
        var text = entity.getBubbleText();

        var positionMatrix = matrices.peek().getPositionMatrix();

        var buffer = vertexConsumers.getBuffer(RenderLayer.getTextBackground());

        matrices.translate(0, .6 + entity.getHeight(), 0);
        matrices.multiply(this.dispatcher.getRotation(), 0, -(entity.getHeight()), 0);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
        matrices.translate(-0.3f, 0, -0.25f);
        matrices.scale(-scaleFactor, -scaleFactor, scaleFactor);

        light = 255;
        vertex(positionMatrix, buffer, 0, 10, 0.01f, 0, 0, light); // top left
        vertex(positionMatrix, buffer, -4, 14, 0.01f, 0, 0, light); // bottom left
        vertex(positionMatrix, buffer, -4, 14, 0.01f, 0, 0, light); // bottom right
        vertex(positionMatrix, buffer, 4, 10, 0.01f, 0, 0, light); // top right

        textRenderer.draw(text, 0, 1, 0x000000, false, positionMatrix, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0xFFFFFFFF, light);
        matrices.pop();
    }

    @Override
    public Identifier getTexture(SpeechBubbleEntity entity) {
        return null;
    }

    public void vertex(Matrix4f positionMatrix, VertexConsumer vertexConsumer, float x, float y, float z, float u, float v, int light) {
        vertexConsumer.vertex(positionMatrix, x, y, z).color(255, 255, 255, 255).texture(u, v).light(light);
    }
}
