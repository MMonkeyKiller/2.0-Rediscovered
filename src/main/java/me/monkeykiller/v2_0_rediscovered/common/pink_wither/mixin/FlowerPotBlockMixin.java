package me.monkeykiller.v2_0_rediscovered.common.pink_wither.mixin;

import me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CarvedPumpkinBlock;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered.CONFIG_COMMON;

@Mixin(FlowerPotBlock.class)
public class FlowerPotBlockMixin {
    @Unique
    private static final BlockPattern witherHugPattern = BlockPatternBuilder.start().aisle("^", "#")
            .where('^', pos -> pos.getBlockState().isOf(Blocks.POTTED_POPPY))
            .where('#', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.PINK_WOOL)))
            .build();


    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", shift = At.Shift.AFTER), method = "onUseWithItem")
    public void onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ItemActionResult> cir) {
        if (!CONFIG_COMMON.pink_wither.enabled) return;
        var result = witherHugPattern.searchAround(world, pos);
        if (result == null) return;
        var witherHugEntity = V2_0_Rediscovered.WITHER_HUG.create(world);
        if (witherHugEntity != null) {
            CarvedPumpkinBlock.breakPatternBlocks(world, result);
            var blockPos = result.translate(0, 1, 0).getBlockPos();
            witherHugEntity.setPosition(blockPos.toCenterPos());
            world.spawnEntity(witherHugEntity);
            CarvedPumpkinBlock.updatePatternBlocks(world, result);
        }
    }
}
