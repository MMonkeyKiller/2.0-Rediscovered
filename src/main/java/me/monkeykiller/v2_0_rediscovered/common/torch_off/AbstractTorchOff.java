package me.monkeykiller.v2_0_rediscovered.common.torch_off;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TorchBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public interface AbstractTorchOff {
    Block getLitVariant();

    default void lit(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand) {
        var itemStack = player.getStackInHand(hand);
        if (!(this instanceof TorchBlock self) || !state.isOf(self)) return;

        SoundEvent sound;
        if (itemStack.isOf(Items.FLINT_AND_STEEL)) {
            sound = SoundEvents.ITEM_FLINTANDSTEEL_USE;
        } else {
            sound = SoundEvents.ITEM_FIRECHARGE_USE;
        }
        world.playSound(player, pos, sound, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);

        var newState = getLitVariant().getStateWithProperties(state);
        world.setBlockState(pos, newState);
        world.emitGameEvent(player, GameEvent.BLOCK_PLACE, pos);

        if (player instanceof ServerPlayerEntity) {
            if (!player.isCreative()) {
                if (itemStack.isOf(Items.FLINT_AND_STEEL)) {
                    itemStack.damage(1, player, (p) -> {
                        p.sendToolBreakStatus(hand);
                    });
                } else {
                    itemStack.decrement(1);
                }
            }
            player.incrementStat(Stats.USED.getOrCreateStat(itemStack.getItem()));
        }
    }
}
