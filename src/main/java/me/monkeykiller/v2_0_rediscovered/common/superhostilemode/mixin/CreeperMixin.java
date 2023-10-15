package me.monkeykiller.v2_0_rediscovered.common.superhostilemode.mixin;

import me.monkeykiller.v2_0_rediscovered.common.superhostilemode.SuperHostileModeInstance;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreeperEntity.class)
public class CreeperMixin {
    @Inject(at = @At("HEAD"), method = "spawnEffectsCloud", cancellable = true)
    private void superHostileModePreventAreaEffectCloud(CallbackInfo ci) {
        var self = (CreeperEntity) ((Object) this);
        if (!(self.getWorld() instanceof ServerWorld serverWorld)) return;
        if (!SuperHostileModeInstance.get(serverWorld).getWaveEntities().contains(self)) return;
        ci.cancel();
    }
}
