package me.monkeykiller.v2_0_rediscovered.client.pink_wither;

import me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered;
import me.monkeykiller.v2_0_rediscovered.common.pink_wither.WitherHugEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class WitherHugEntityRenderer extends MobEntityRenderer<WitherHugEntity, WitherHugEntityModel> {
    private static final Identifier INVULNERABLE_TEXTURE = V2_0_Rediscovered.identifier("textures/entity/wither_pink/wither_pink_invulnerable.png");
    private static final Identifier TEXTURE = V2_0_Rediscovered.identifier("textures/entity/wither_pink/wither_pink.png");

    public WitherHugEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new WitherHugEntityModel(context.getPart(EntityModelLayers.WITHER)), 1.0f);
        this.addFeature(new WitherHugArmorFeatureRenderer(this, context.getModelLoader()));
    }

    @Override
    protected int getBlockLight(WitherHugEntity witherEntity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public Identifier getTexture(WitherHugEntity witherEntity) {
        int i = witherEntity.getInvulnerableTimer();
        if (i <= 0 || i <= 80 && i / 5 % 2 == 1) {
            return TEXTURE;
        }
        return INVULNERABLE_TEXTURE;
    }

    @Override
    protected void scale(WitherHugEntity witherEntity, MatrixStack matrixStack, float f) {
        float g = 2.0f;
        int i = witherEntity.getInvulnerableTimer();
        if (i > 0) {
            g -= ((float)i - f) / 220.0f * 0.5f;
        }
        matrixStack.scale(g, g, g);
    }
}
