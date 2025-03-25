package me.monkeykiller.v2_0_rediscovered.common.etho_slab.mixin;

import me.monkeykiller.v2_0_rediscovered.common.etho_slab.EthoEntityAccessor;
import net.minecraft.block.Blocks;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered.CONFIG_COMMON;

@Mixin(TntEntity.class)
public abstract class TntEntityMixin implements EthoEntityAccessor {
    @Unique
    private static final TrackedData<String> ETHO_TYPE = DataTracker.registerData(TntEntity.class, TrackedDataHandlerRegistry.STRING);

    @Inject(at = @At("TAIL"), method = "initDataTracker")
    protected void injectDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(ETHO_TYPE, EthoType.NONE.toString());
    }

    @Inject(at = @At("TAIL"), method = "readCustomDataFromNbt")
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        var self = (TntEntity) (Object) this;
        self.getDataTracker().set(ETHO_TYPE, nbt.getString("EthoType"));
    }

    @Inject(at = @At("TAIL"), method = "writeCustomDataToNbt")
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        var self = (TntEntity) (Object) this;
        nbt.putString("EthoType", self.getDataTracker().get(ETHO_TYPE));
    }

    @Override
    public EthoType getEthoType() {
        try {
            var self = (TntEntity) (Object) this;
            return EthoType.valueOf(self.getDataTracker().get(ETHO_TYPE));
        } catch (IllegalArgumentException e) {
            return EthoType.NONE;
        }
    }

    @Override
    public void setEthoType(EthoType ethoType) {
        var self = (TntEntity) (Object) this;
        self.getDataTracker().set(ETHO_TYPE, ethoType.toString());
    }

    @Inject(at = @At("TAIL"), method = "explode")
    public void ethoExplode(CallbackInfo ci) {
        var self = (TntEntity) (Object) this;
        var random = self.getWorld().random;

        if (random.nextFloat() < 0.25F && this.getEthoType() != EthoType.NONE) {
            var closestPlayer = self.getWorld().getClosestPlayer(
                    self.getX(), self.getY(), self.getZ(),
                    20.0D, false);
            if (closestPlayer != null) {
                var fallingBlock = FallingBlockEntityInvoker.newFallingBlockEntity(self.getWorld(),
                        closestPlayer.getX(), closestPlayer.getBoundingBox().maxY + 1.0D, closestPlayer.getZ(),
                        Blocks.ANVIL.getDefaultState());
                if (!CONFIG_COMMON.etho_slab.should_place_anvil) {
                    fallingBlock.setDestroyedOnLanding();
                }
                fallingBlock.setHurtEntities(40.0F, 40);
                self.getWorld().spawnEntity(fallingBlock);
            }
        }
    }
}
