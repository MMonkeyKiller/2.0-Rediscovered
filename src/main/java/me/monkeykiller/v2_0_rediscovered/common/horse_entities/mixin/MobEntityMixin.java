package me.monkeykiller.v2_0_rediscovered.common.horse_entities.mixin;

import me.monkeykiller.v2_0_rediscovered.common.horse_entities.FatEntityAccessor;
import me.monkeykiller.v2_0_rediscovered.common.horse_entities.HorseEntityAccessor;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// TODO: separate fatness to a different mixin package
@Mixin(MobEntity.class)
public abstract class MobEntityMixin implements HorseEntityAccessor, FatEntityAccessor {
    private static final TrackedData<Byte> FATNESS = DataTracker.registerData(MobEntity.class, TrackedDataHandlerRegistry.BYTE);

    @Inject(at = @At("TAIL"), method = "initDataTracker")
    protected void injectDataTracker(CallbackInfo ci) {
        var self = (MobEntity) (Object) this;
        if (self instanceof CowEntity || self instanceof PigEntity) {
            setupDataTracker();
        }
        if (self instanceof AnimalEntity) {
            self.getDataTracker().startTracking(FATNESS, (byte) 0);
        }
    }

    @Inject(at = @At("TAIL"), method = "initialize")
    public void initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, NbtCompound entityNbt, CallbackInfoReturnable<EntityData> cir) {
        var self = (MobEntity) (Object) this;
        if (self instanceof CowEntity || self instanceof PigEntity) {
            setupHorse();
        }
    }

    @Inject(at = @At("TAIL"), method = "readCustomDataFromNbt")
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        var self = ((MobEntity) (Object) this);
        if (self instanceof CowEntity || self instanceof PigEntity) {
            setHorse(nbt.getBoolean("IsHorse"));
        }
        if (self instanceof AnimalEntity) {
            setFatness(nbt.getByte("Fatness"));
        }
    }

    @Inject(at = @At("TAIL"), method = "writeCustomDataToNbt")
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        var self = ((MobEntity) (Object) this);
        if (self instanceof CowEntity || self instanceof PigEntity) {
            nbt.putBoolean("IsHorse", isHorse());
        }
        if (self instanceof AnimalEntity) {
            nbt.putByte("Fatness", getFatness());
        }
    }

    @Override
    public byte getFatness() {
        var self = ((MobEntity) (Object) this);
        return self.getDataTracker().get(FATNESS);
    }

    @Override
    public void setFatness(int value) {
        var self = ((MobEntity) (Object) this);
        self.getDataTracker().set(FATNESS, (byte) value);
    }

    @Inject(at = @At("TAIL"), method = "interact", cancellable = true)
    public void fatnessInteract(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        var self = ((MobEntity) (Object) this);
        if (!(self instanceof AnimalEntity)) return;
        int newFatness;

        var item = player.getStackInHand(hand);

        if (item == null || !((AnimalEntity) self).isBreedingItem(item)) return;

        if (self.getWorld() instanceof ServerWorld serverWorld && !self.isBaby() && !(self instanceof WolfEntity) && !(self instanceof CatEntity) && !(self instanceof OcelotEntity)) {
            if (!player.getAbilities().creativeMode) item.decrement(1);
            newFatness = this.getFatness() + 1;

            if (newFatness > 9) {
                self.discard();
                self.getWorld().createExplosion(self, self.getX(), (self.getBoundingBox().minY + self.getBoundingBox().maxY) / 2D, self.getZ(), 0.0F, World.ExplosionSourceType.NONE);
                int var4 = self.getRandom().nextInt(newFatness * 2);

                for (int i = 0; i < var4; ++i) {
                    var lootTable = serverWorld.getServer().getLootManager().getLootTable(self.getLootTable());
                    var builder = new LootContextParameterSet.Builder(serverWorld)
                            // .random(self.getRandom().nextLong())
                            .add(LootContextParameters.THIS_ENTITY, self)
                            .add(LootContextParameters.ORIGIN, self.getPos())
                            .add(LootContextParameters.DAMAGE_SOURCE, self.getDamageSources().fall());
                    var list = lootTable.generateLoot(builder.build(LootContextTypes.ENTITY));
                    if (list.isEmpty()) continue;
                    var stack = list.get(self.getRandom().nextInt(list.size()));

                    var itemEntity = new ItemEntity(self.getWorld(), self.getX(), self.getY() + 0.3D,
                            self.getZ(), stack);

                    itemEntity.setPickupDelay(40);
                    float var7 = self.getRandom().nextFloat() * 0.2F;
                    itemEntity.setVelocity(
                            self.getRandom().nextGaussian() * (double) var7,
                            (self.getRandom().nextFloat() * 0.3F) + 0.2F,
                            self.getRandom().nextGaussian() * (double) var7
                    );
                    self.getWorld().spawnEntity(itemEntity);
                }
            }

            this.setFatness(newFatness);
        }
        if (self.getWorld().isClient()) cir.setReturnValue(ActionResult.CONSUME);
    }
}
