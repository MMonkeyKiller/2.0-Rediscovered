package me.monkeykiller.v2_0_rediscovered.common.etho_slab.mixin;

import me.monkeykiller.v2_0_rediscovered.common.etho_slab.EthoEntityAccessor;
import net.minecraft.block.Blocks;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TntEntity.class)
public abstract class TntEntityMixin implements EthoEntityAccessor {
    private static final TrackedData<Boolean> IS_ETHO = DataTracker.registerData(TntEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Inject(at = @At("TAIL"), method = "initDataTracker")
    protected void injectDataTracker(CallbackInfo ci) {
        var self = (TntEntity) (Object) this;
        self.getDataTracker().startTracking(IS_ETHO, false);
    }

    @Inject(at = @At("TAIL"), method = "readCustomDataFromNbt")
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        var self = (TntEntity) (Object) this;
        self.getDataTracker().set(IS_ETHO, nbt.getBoolean("IsEtho"));
    }

    @Inject(at = @At("TAIL"), method = "writeCustomDataToNbt")
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        var self = (TntEntity) (Object) this;
        nbt.putBoolean("IsEtho", self.getDataTracker().get(IS_ETHO));
    }

    @Override
    public boolean isEtho() {
        var self = (TntEntity) (Object) this;
        return self.getDataTracker().get(IS_ETHO);
    }

    @Override
    public void setEtho(boolean isEtho) {
        var self = (TntEntity) (Object) this;
        self.getDataTracker().set(IS_ETHO, isEtho);
    }

    @Inject(at = @At("TAIL"), method = "explode")
    public void ethoExplode(CallbackInfo ci) {
        var self = (TntEntity) (Object) this;
        var random = self.world.random;

        if (random.nextFloat() < 0.25F && this.isEtho()) {
            var closestPlayer = self.world.getClosestPlayer(
                    self.getX(), self.getY(), self.getZ(),
                    20.0D, false);
            if (closestPlayer != null) {
                var fallingBlock = FallingBlockEntityInvoker.newFallingBlockEntity(self.world,
                        closestPlayer.getX(), closestPlayer.getBoundingBox().maxY + 1.0D, closestPlayer.getZ(),
                        Blocks.ANVIL.getDefaultState());
                fallingBlock.setHurtEntities(40.0F, 40);
                self.world.spawnEntity(fallingBlock);
            }
        }
    }
}
