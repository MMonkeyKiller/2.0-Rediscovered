package me.monkeykiller.v2_0_rediscovered.common.fat_entities.mixin;

import me.monkeykiller.v2_0_rediscovered.common.fat_entities.FatEntityAccessor;
import net.minecraft.entity.ItemEntity;
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
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered.CONFIG_COMMON;

@Mixin(MobEntity.class)
public class MobEntityMixin implements FatEntityAccessor {
    @Unique
    private static final TrackedData<Byte> FATNESS = DataTracker.registerData(MobEntity.class, TrackedDataHandlerRegistry.BYTE);

    @Inject(at = @At("TAIL"), method = "initDataTracker")
    protected void injectDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(FATNESS, (byte) 0);
    }

    @Inject(at = @At("TAIL"), method = "readCustomDataFromNbt")
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if (CONFIG_COMMON.mob_fatness.enabled && isFattenable()) {
            setFatness(nbt.getByte("Fatness"));
        }
    }

    @Inject(at = @At("TAIL"), method = "writeCustomDataToNbt")
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        if (isFattenable()) {
            nbt.putByte("Fatness", getFatness());
        }
    }

    @Inject(at = @At("TAIL"), method = "interact", cancellable = true)
    public void fatnessInteract(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        var self = (MobEntity) (Object) this;
        if (!CONFIG_COMMON.mob_fatness.enabled || !isFattenable()) return;
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
                    var lootTable = serverWorld.getServer().getReloadableRegistries().getLootTable(self.getLootTable());
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

    @Override
    public boolean isFattenable() {
        var self = (MobEntity) (Object) this;
        return self instanceof CowEntity /* or Mooshroom */
                || self instanceof SheepEntity
                || self instanceof PigEntity
                || self instanceof ChickenEntity;
    }

    @Override
    public byte getFatness() {
        var self = (MobEntity) (Object) this;
        return self.getDataTracker().get(FATNESS);
    }

    @Override
    public void setFatness(int value) {
        var self = (MobEntity) (Object) this;
        self.getDataTracker().set(FATNESS, (byte) value);
    }
}
