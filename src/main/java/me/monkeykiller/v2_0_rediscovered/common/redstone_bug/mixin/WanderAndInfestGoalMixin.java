package me.monkeykiller.v2_0_rediscovered.common.redstone_bug.mixin;

import me.monkeykiller.v2_0_rediscovered.common.redstone_bug.RedstoneBugAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.entity.mob.SilverfishEntity$WanderAndInfestGoal")
public abstract class WanderAndInfestGoalMixin extends WanderAroundGoalAccesor {
    @Accessor("canInfest")
    @SuppressWarnings("SameParameterValue")
    abstract void setCanInfest(boolean canInfest);

    @Inject(at = @At("HEAD"), method = "start")
    private void start(CallbackInfo ci) {
        var redstoneBug = (RedstoneBugAccessor) this.mob;
        if (redstoneBug.getColor() != 0) {
            this.setCanInfest(false);
        }
    }
}
