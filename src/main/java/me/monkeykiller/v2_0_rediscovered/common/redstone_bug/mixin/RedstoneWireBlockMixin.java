package me.monkeykiller.v2_0_rediscovered.common.redstone_bug.mixin;

import me.monkeykiller.v2_0_rediscovered.common.redstone_bug.RedstoneBugAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered.CONFIG_COMMON;

@Mixin(RedstoneWireBlock.class)
public abstract class RedstoneWireBlockMixin {
    @Shadow
    @Final
    public static IntProperty POWER;

    @Shadow
    protected abstract int getReceivedRedstonePower(World world, BlockPos pos);

    @Inject(at = @At("TAIL"), method = "update")
    public void update(World world, BlockPos pos, BlockState state, CallbackInfo ci) {
        if (!CONFIG_COMMON.redstone_bugs.enabled) return;
        var power = state.get(POWER);
        var receivedPower = this.getReceivedRedstonePower(world, pos);
        var random = world.getRandom();

        if (power < receivedPower) {
            var probability = CONFIG_COMMON.redstone_bugs.base_spawn_probability; // Initial probability for the first silverfish
            byte radius = 3;
            probability += (float) world.getNonSpectatingEntities(SilverfishEntity.class, Box.of(pos.toCenterPos(), radius * 2, radius * 2, radius * 2)).size() * CONFIG_COMMON.redstone_bugs.spawn_probability_multiplier;

            if (random.nextFloat() < probability) {
                var silverfish = new SilverfishEntity(EntityType.SILVERFISH, world);
                silverfish.setPosition(pos.toCenterPos());
                var color = (int) (MathHelper.nextBetween(random, 0.5F, 1.0F) * 255.0F) << 18;
                ((RedstoneBugAccessor) silverfish).setColor(color);
                world.spawnEntity(silverfish);
            }
        }
    }
}
