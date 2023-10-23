package me.monkeykiller.v2_0_rediscovered.client.pink_wither;

import me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered;
import me.monkeykiller.v2_0_rediscovered.common.pink_wither.WitherLoveEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.SkullEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class WitherLoveEntityRenderer extends EntityRenderer<WitherLoveEntity> {
    private static final Identifier TEXTURE = V2_0_Rediscovered.identifier("textures/entity/wither_pink/wither_pink.png");
    private final SkullEntityModel model;

    public WitherLoveEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new SkullEntityModel(context.getPart(EntityModelLayers.WITHER_SKULL));
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 35).cuboid(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f), ModelTransform.NONE);
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    protected int getBlockLight(WitherLoveEntity witherLoveEntity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public void render(WitherLoveEntity witherLoveEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.scale(-1.0f, -1.0f, 1.0f);
        float h = MathHelper.lerpAngleDegrees(g, witherLoveEntity.prevYaw, witherLoveEntity.getYaw());
        float j = MathHelper.lerp(g, witherLoveEntity.prevPitch, witherLoveEntity.getPitch());
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(this.model.getLayer(this.getTexture(witherLoveEntity)));
        this.model.setHeadRotation(0.0f, h, j);
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
        matrixStack.pop();
        super.render(witherLoveEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(WitherLoveEntity witherLoveEntity) {
        return TEXTURE;
    }
}
