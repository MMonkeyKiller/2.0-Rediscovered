package me.monkeykiller.v2_0_rediscovered.common.torch_off.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered.*;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {
    @Inject(at = @At("HEAD"), method = "randomTick")
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (!CONFIG_COMMON.burnt_out_torches.enabled) return;
        if (state.isOf(Blocks.TORCH) || state.isOf(Blocks.WALL_TORCH)) {
            if (state.isOf(Blocks.TORCH)) {
                world.setBlockState(pos, TORCH_OFF_BLOCK.getStateWithProperties(state));
            } else {
                world.setBlockState(pos, WALL_TORCH_OFF_BLOCK.getStateWithProperties(state));
            }
            world.playSound(null, pos,
                    SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5f,
                    2.6F + (random.nextFloat() - random.nextFloat()) * 0.8F);
            world.addParticle(ParticleTypes.SMOKE,
                    pos.getX(), pos.getY(), pos.getZ(),
                    0.0D, 0.0D, 0.0D);
        }
    }
}
