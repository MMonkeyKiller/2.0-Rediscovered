package me.monkeykiller.v2_0_rediscovered.client.redstone_bug.mixin;

import me.monkeykiller.v2_0_rediscovered.common.redstone_bug.RedstoneBugAccessor;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.SilverfishEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @Unique
    private int color;

    @Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
    public <T extends LivingEntity> void checkSilverfish(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (livingEntity instanceof SilverfishEntity silverfish) {
            this.color = ((RedstoneBugAccessor) silverfish).getColor();
        }
    }

    @ModifyArgs(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V"), method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
    protected void modifyArgs(Args args) {
        if (color != 0) {
            var red = (float) (color >> 16 & 255) / 255.0F;
            var green = (float) (color >> 8 & 255) / 255.0F;
            var blue = (float) (color & 255) / 255.0F;
            var alpha = (float) (color >> 24 & 255) / 255.0F;

            var i = 4;
            args.set(++i, red);
            args.set(++i, green);
            args.set(++i, blue);
            args.set(++i, alpha);
        }
    }
}
