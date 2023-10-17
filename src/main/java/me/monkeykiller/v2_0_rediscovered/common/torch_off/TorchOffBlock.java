package me.monkeykiller.v2_0_rediscovered.common.torch_off;

import net.minecraft.block.BlockState;
import net.minecraft.block.TorchBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class TorchOffBlock extends TorchBlock {
    public TorchOffBlock(Settings settings) {
        super(settings, null);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
    }
}
