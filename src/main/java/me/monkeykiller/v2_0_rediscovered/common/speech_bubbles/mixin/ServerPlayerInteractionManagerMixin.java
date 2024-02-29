package me.monkeykiller.v2_0_rediscovered.common.speech_bubbles.mixin;

import me.monkeykiller.v2_0_rediscovered.common.speech_bubbles.SpeechBubbleEntity;
import net.minecraft.block.BlockState;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.Stream;

import static me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered.CONFIG_COMMON;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class ServerPlayerInteractionManagerMixin {
    @Shadow
    protected ServerWorld world;

    @Inject(at = @At(value = "TAIL"), method = "continueMining")
    private void injectBubbleSpawn(BlockState state, BlockPos pos, int failedStartMiningTime, CallbackInfoReturnable<Float> cir) {
        if (!CONFIG_COMMON.speech_bubbles.enabled) return;
        float prob = (float) (Stream.of(BlockTags.COAL_ORES, BlockTags.COPPER_ORES, BlockTags.DIAMOND_ORES, BlockTags.GOLD_ORES, BlockTags.EMERALD_ORES, BlockTags.IRON_ORES, BlockTags.REDSTONE_ORES, BlockTags.LAPIS_ORES)
                .anyMatch(state::isIn) ? CONFIG_COMMON.speech_bubbles.spawn_chance_ores : CONFIG_COMMON.speech_bubbles.spawn_chance_blocks);

        if (this.world.random.nextFloat() >= prob) return;
        var bubbles = this.world.getEntitiesByClass(SpeechBubbleEntity.class, Box.from(pos.toCenterPos()), e -> true);
        if (bubbles.isEmpty()) {
            var speechBubble = new SpeechBubbleEntity(this.world, pos.getX(), pos.getY(), pos.getZ());
            speechBubble.setBubbleText(SpeechBubbleEntity.getRandomText());
            this.world.spawnEntity(speechBubble);
        }
    }
}
