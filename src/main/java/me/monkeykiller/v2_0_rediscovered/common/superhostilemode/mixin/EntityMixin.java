package me.monkeykiller.v2_0_rediscovered.common.superhostilemode.mixin;

import me.monkeykiller.v2_0_rediscovered.common.superhostilemode.SuperHostileModeInstance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(at = @At("HEAD"), method = "saveSelfNbt", cancellable = true)
    public void superHostileModePreventEntitySaving(NbtCompound nbt, CallbackInfoReturnable<Boolean> cir) {
        final var self = (Entity) ((Object) this);
        if (!(self instanceof MobEntity && self.getWorld() instanceof ServerWorld serverWorld)) return;
        if (SuperHostileModeInstance.get(serverWorld).getWaveEntities().contains(self))
            cir.setReturnValue(false);
    }
}
