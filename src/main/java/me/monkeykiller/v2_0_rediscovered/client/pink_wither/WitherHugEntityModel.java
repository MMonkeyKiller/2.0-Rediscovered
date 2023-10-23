package me.monkeykiller.v2_0_rediscovered.client.pink_wither;

import lombok.Getter;
import me.monkeykiller.v2_0_rediscovered.common.pink_wither.WitherHugEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.util.math.MathHelper;

public class WitherHugEntityModel extends SinglePartEntityModel<WitherHugEntity> {
    private static final String RIBCAGE = "ribcage";

    private static final String CENTER_HEAD = "center_head";

    private static final String RIGHT_HEAD = "right_head";

    private static final String LEFT_HEAD = "left_head";
    private final ModelPart root;

    @Getter
    private final ModelPart centerHead;
    @Getter
    private final ModelPart rightHead;
    @Getter
    private final ModelPart leftHead;

    private final ModelPart ribcage;
    private final ModelPart tail;

    public WitherHugEntityModel(ModelPart root) {
        this.root = root;
        this.ribcage = root.getChild(RIBCAGE);
        this.tail = root.getChild(EntityModelPartNames.TAIL);
        this.centerHead = root.getChild(CENTER_HEAD);
        this.rightHead = root.getChild(RIGHT_HEAD);
        this.leftHead = root.getChild(LEFT_HEAD);
    }

    public static TexturedModelData getTexturedModelData(Dilation dilation) {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("shoulders", ModelPartBuilder.create().uv(0, 16).cuboid(-10.0f, 3.9f, -0.5f, 20.0f, 3.0f, 3.0f, dilation), ModelTransform.NONE);
        modelPartData.addChild(RIBCAGE, ModelPartBuilder.create().uv(0, 22).cuboid(0.0f, 0.0f, 0.0f, 3.0f, 10.0f, 3.0f, dilation).uv(24, 22).cuboid(-4.0f, 1.5f, 0.5f, 11.0f, 2.0f, 2.0f, dilation).uv(24, 22).cuboid(-4.0f, 4.0f, 0.5f, 11.0f, 2.0f, 2.0f, dilation).uv(24, 22).cuboid(-4.0f, 6.5f, 0.5f, 11.0f, 2.0f, 2.0f, dilation), ModelTransform.of(-2.0f, 6.9f, -0.5f, 0.20420352f, 0.0f, 0.0f));
        modelPartData.addChild(EntityModelPartNames.TAIL, ModelPartBuilder.create().uv(12, 22).cuboid(0.0f, 0.0f, 0.0f, 3.0f, 6.0f, 3.0f, dilation), ModelTransform.of(-2.0f, 6.9f + MathHelper.cos(0.20420352f) * 10.0f, -0.5f + MathHelper.sin(0.20420352f) * 10.0f, 0.83252203f, 0.0f, 0.0f));
        modelPartData.addChild(CENTER_HEAD, ModelPartBuilder.create().uv(0, 0).cuboid(-4.0f, -4.0f, -4.0f, 8.0f, 8.0f, 8.0f, dilation), ModelTransform.NONE);
        ModelPartBuilder modelPartBuilder = ModelPartBuilder.create().uv(32, 0).cuboid(-4.0f, -4.0f, -4.0f, 6.0f, 6.0f, 6.0f, dilation);
        modelPartData.addChild(RIGHT_HEAD, modelPartBuilder, ModelTransform.pivot(-8.0f, 4.0f, 0.0f));
        modelPartData.addChild(LEFT_HEAD, modelPartBuilder, ModelTransform.pivot(10.0f, 4.0f, 0.0f));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public ModelPart getPart() {
        return this.root;
    }

    @Override
    public void setAngles(WitherHugEntity witherEntity, float f, float g, float h, float i, float j) {
        float k = MathHelper.cos(h * 0.1f);
        this.ribcage.pitch = (0.065f + 0.05f * k) * (float) Math.PI;
        this.tail.setPivot(-2.0f, 6.9f + MathHelper.cos(this.ribcage.pitch) * 10.0f, -0.5f + MathHelper.sin(this.ribcage.pitch) * 10.0f);
        this.tail.pitch = (0.265f + 0.1f * k) * (float) Math.PI;
        this.centerHead.yaw = i * ((float) Math.PI / 180);
        this.centerHead.pitch = j * ((float) Math.PI / 180);
    }

    @Override
    public void animateModel(WitherHugEntity witherEntity, float f, float g, float h) {
        var shoulders = this.root.getChild("shoulders");
        shoulders.xScale = (5 + Math.min(2, witherEntity.getHeads()) * 7.5f) / 20f;
        if (witherEntity.getHeads() == 0) {
            shoulders.pivotX = -0.5f;
        } else if (witherEntity.getHeads() == 1) {
            shoulders.pivotX = -4f;
        } else {
            shoulders.pivotX = 0;
        }

        this.leftHead.visible = witherEntity.getHeads() > 1;
        this.rightHead.visible = witherEntity.getHeads() > 0;

        rotateHead(witherEntity, this.rightHead);
        rotateHead(witherEntity, this.leftHead);
    }

    private static void rotateHead(WitherHugEntity entity, ModelPart head) {
        head.yaw = (entity.getHeadYaw() - entity.bodyYaw) * ((float) Math.PI / 180);
        head.pitch = entity.getPitch() * ((float) Math.PI / 180);
    }
}
