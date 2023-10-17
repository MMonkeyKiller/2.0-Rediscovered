package me.monkeykiller.v2_0_rediscovered.client;

import me.monkeykiller.v2_0_rediscovered.client.speech_bubbles.SpeechBubblesRenderer;
import me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;

public class V2_0_RediscoveredClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(
                V2_0_Rediscovered.SPEECH_BUBBLE,
                SpeechBubblesRenderer::new
        );

        BlockRenderLayerMap.INSTANCE.putBlock(V2_0_Rediscovered.TORCH_OFF_BLOCK, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(V2_0_Rediscovered.WALL_TORCH_OFF_BLOCK, RenderLayer.getCutout());
    }
}
