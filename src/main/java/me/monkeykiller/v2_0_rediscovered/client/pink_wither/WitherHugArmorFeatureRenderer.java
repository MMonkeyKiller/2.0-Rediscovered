package me.monkeykiller.v2_0_rediscovered.client.pink_wither;

import me.monkeykiller.v2_0_rediscovered.common.pink_wither.WitherHugEntity;
import net.minecraft.client.render.entity.feature.EnergySwirlOverlayFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class WitherHugArmorFeatureRenderer extends EnergySwirlOverlayFeatureRenderer<WitherHugEntity, WitherHugEntityModel> {
    private static final Identifier SKIN = Identifier.ofVanilla("textures/entity/wither/wither_armor.png");
    private final WitherHugEntityModel model;

    public WitherHugArmorFeatureRenderer(FeatureRendererContext<WitherHugEntity, WitherHugEntityModel> context, EntityModelLoader loader) {
        super(context);
        this.model = new WitherHugEntityModel(loader.getModelPart(EntityModelLayers.WITHER_ARMOR));
    }

    @Override
    protected float getEnergySwirlX(float partialAge) {
        return MathHelper.cos(partialAge * 0.02f) * 3.0f;
    }

    @Override
    protected Identifier getEnergySwirlTexture() {
        return SKIN;
    }

    @Override
    protected EntityModel<WitherHugEntity> getEnergySwirlModel() {
        return this.model;
    }
}
