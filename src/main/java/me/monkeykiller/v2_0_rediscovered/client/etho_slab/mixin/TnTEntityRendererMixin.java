package me.monkeykiller.v2_0_rediscovered.client.etho_slab.mixin;

import me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered;
import me.monkeykiller.v2_0_rediscovered.common.etho_slab.EthoEntityAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.TntEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.TntEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TntEntityRenderer.class)
public class TnTEntityRendererMixin {
    @Unique
    private EthoEntityAccessor capturedTnTEntity;

    // Inject into the render method and capture the TntEntity instance
    @Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/entity/TntEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
    public void captureTntEntity(TntEntity tntEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        this.capturedTnTEntity = (EthoEntityAccessor) tntEntity;
    }

    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/TntMinecartEntityRenderer;renderFlashingBlock(Lnet/minecraft/client/render/block/BlockRenderManager;Lnet/minecraft/block/BlockState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IZ)V"), method = "render(Lnet/minecraft/entity/TntEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
    public BlockState render(BlockState state) {
        var ethoType = capturedTnTEntity.getEthoType();
        if (ethoType != EthoEntityAccessor.EthoType.NONE) {
            return V2_0_Rediscovered.ETHO_SLAB_BLOCK.getDefaultState()
                    .with(SlabBlock.TYPE, ethoType.getSlabType());
        }
        return state;
    }
}
