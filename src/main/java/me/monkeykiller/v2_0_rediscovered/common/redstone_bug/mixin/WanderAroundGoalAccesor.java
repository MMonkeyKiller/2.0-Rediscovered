package me.monkeykiller.v2_0_rediscovered.common.redstone_bug.mixin;

import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WanderAroundGoal.class)
public abstract class WanderAroundGoalAccesor {
    @Final
    @Shadow
    protected PathAwareEntity mob;
}
