package me.monkeykiller.v2_0_rediscovered.client;

import me.monkeykiller.v2_0_rediscovered.client.speech_bubbles.SpeechBubblesRenderer;
import me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class V2_0_RediscoveredClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(
                V2_0_Rediscovered.SPEECH_BUBBLE,
                SpeechBubblesRenderer::new
        );
    }
}
