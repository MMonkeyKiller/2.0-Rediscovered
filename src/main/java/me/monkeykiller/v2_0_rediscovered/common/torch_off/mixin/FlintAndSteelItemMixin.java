package me.monkeykiller.v2_0_rediscovered.common.torch_off.mixin;

import me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Blocks;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlintAndSteelItem.class)
public class FlintAndSteelItemMixin {
    @Inject(at = @At("HEAD"), method = "useOnBlock", cancellable = true)
    public void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        var player = context.getPlayer();
        var world = context.getWorld();
        var pos = context.getBlockPos();
        var state = world.getBlockState(pos);

        if (state.isOf(V2_0_Rediscovered.TORCH_OFF_BLOCK) || state.isOf(V2_0_Rediscovered.WALL_TORCH_OFF_BLOCK)) {
            world.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.getRandom().nextFloat() * 0.4F + 0.8F);
            if (state.isOf(V2_0_Rediscovered.TORCH_OFF_BLOCK)) {
                world.setBlockState(pos, Blocks.TORCH.getDefaultState());
            } else {
                var newState = Blocks.WALL_TORCH.getDefaultState()
                        .withIfExists(WallTorchBlock.FACING, state.get(WallTorchBlock.FACING));
                world.setBlockState(pos, newState);
            }

            world.emitGameEvent(player, GameEvent.BLOCK_PLACE, pos);
            var itemStack = context.getStack();
            if (player instanceof ServerPlayerEntity serverPlayer) {
                Criteria.PLACED_BLOCK.trigger(serverPlayer, pos, itemStack);
                itemStack.damage(1, player, (p) -> {
                    p.sendToolBreakStatus(context.getHand());
                });
            }
            cir.setReturnValue(ActionResult.success(world.isClient()));
        }
    }
}
