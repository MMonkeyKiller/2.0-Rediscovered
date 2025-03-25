package me.monkeykiller.v2_0_rediscovered.common.torch_off;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TorchBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class TorchOffBlock extends TorchBlock implements AbstractTorchOff {
    public TorchOffBlock(Settings settings) {
        super(null, settings);
    }

    @Override
    public Block getLitVariant() {
        return Blocks.TORCH;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        var itemStack = player.getStackInHand(hand);
        if (!itemStack.isOf(Items.FLINT_AND_STEEL) && !itemStack.isOf(Items.FIRE_CHARGE)) {
            return super.onUseWithItem(itemStack, state, world, pos, player, hand, hit);
        }

        lit(state, world, pos, player, hand);
        return ItemActionResult.success(world.isClient());
    }
}
