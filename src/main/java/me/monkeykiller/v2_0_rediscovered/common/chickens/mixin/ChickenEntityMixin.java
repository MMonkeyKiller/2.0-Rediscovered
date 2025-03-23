package me.monkeykiller.v2_0_rediscovered.common.chickens.mixin;

import me.monkeykiller.v2_0_rediscovered.common.chickens.DiamondChickenAccessor;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered.CONFIG_COMMON;

@Mixin(ChickenEntity.class)
public abstract class ChickenEntityMixin extends MobEntity implements DiamondChickenAccessor {
    @Unique
    private static final TrackedData<Boolean> DIAMOND_CHICKEN = DataTracker.registerData(ChickenEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Unique
    int anger = 0;

    protected ChickenEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        getDataTracker().startTracking(DIAMOND_CHICKEN, false);
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        var data = super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
        if (CONFIG_COMMON.diamond_chickens.enabled) {
            if (this.random.nextFloat() < CONFIG_COMMON.diamond_chickens.spawn_probability) {
                this.setDiamondChicken(true);
            }
        }
        return data;
    }

    public boolean isDiamondChicken() {
        return getDataTracker().get(DIAMOND_CHICKEN);
    }

    public void setDiamondChicken(boolean value) {
        getDataTracker().set(DIAMOND_CHICKEN, value);
    }

    @Inject(at = @At("RETURN"), method = "createChickenAttributes", cancellable = true)
    private static void createMobAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        var attributes = cir.getReturnValue();
        if (CONFIG_COMMON.neutral_chickens.enabled) {
            attributes = attributes.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0);
        }
        cir.setReturnValue(attributes);
    }

    @Inject(at = @At("TAIL"), method = "initGoals")
    protected void injectCustomGoals(CallbackInfo ci) {
        var self = (ChickenEntity) (Object) this;
        if (CONFIG_COMMON.neutral_chickens.enabled) {
            this.goalSelector.add(4, new MeleeAttackGoal(self, 1f, true));
            this.targetSelector.add(3, new RevengeGoal(self));
        }
    }

    @Redirect(method = "initGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/goal/GoalSelector;add(ILnet/minecraft/entity/ai/goal/Goal;)V"))
    public void add(GoalSelector instance, int priority, Goal goal) {
      if (goal instanceof EscapeDangerGoal && CONFIG_COMMON.neutral_chickens.enabled) return;
      instance.add(priority, goal);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/AnimalEntity;tickMovement()V", shift = At.Shift.AFTER), method = "tickMovement")
    public void getNearestAttackTarget(CallbackInfo ci) {
        if (!CONFIG_COMMON.neutral_chickens.enabled) return;
        if (!this.isBaby() && this.getTarget() == null) {
            this.setTarget(this.getNearestPlayerToAttack());
        }
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/ChickenEntity;dropItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/entity/ItemEntity;"), method = "tickMovement")
    public ItemEntity customLayEgg(ChickenEntity instance, ItemConvertible itemConvertible) {
        if (CONFIG_COMMON.diamond_chickens.enabled && isDiamondChicken()) {
            if (CONFIG_COMMON.diamond_chickens.should_explode && random.nextFloat() < 0.05D) {
                this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 2.0F, false, World.ExplosionSourceType.MOB);
                return null;
            } else if (CONFIG_COMMON.diamond_chickens.special_drops && random.nextFloat() <= CONFIG_COMMON.diamond_chickens.special_drop_chance) {
                if (random.nextBoolean()) {
                    return this.dropItem(Items.DIAMOND);
                } else {
                    return this.dropItem(Items.LAPIS_LAZULI);
                }
            }
        }
        return this.dropItem(itemConvertible);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (!super.damage(source, amount)) return false;
        if (!CONFIG_COMMON.neutral_chickens.enabled) return true;

        if (this.random.nextFloat() < 0.25f && source.getAttacker() instanceof ServerPlayerEntity player) {
            var chickens = this.getWorld().getEntitiesByClass(ChickenEntity.class, getBoundingBox().expand(10D, 10D, 10D), e -> e != (MobEntity) this);
            var size = chickens.size();
            if (CONFIG_COMMON.neutral_chickens.spawn_clones) {
                for (int i = MathHelper.nextInt(this.random, 1, 3); size < i; ++size) {
                    var chicken = new ChickenEntity(EntityType.CHICKEN, getWorld());
                    chicken.setPosition(this.getX(), this.getY() + 1.5D, this.getZ());
                    chicken.setYaw(this.getYaw());
                    chicken.setPitch(this.getPitch());
                    chicken.setTarget(player);
                    this.getWorld().spawnEntity(chicken);
                }
            }
        }
        return true;
    }

    protected PlayerEntity getNearestPlayerToAttack() {
        var player = this.getWorld().getClosestPlayer(this, 64.0D);
        if (player == null) return null;
        if (this.isPlayerStaring(player)) {
            if (this.anger == 0) {
                this.getWorld().playSound(null, getBlockPos(), SoundEvents.ENTITY_CHICKEN_AMBIENT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            }

            if (this.anger++ == 5) {
                this.anger = 0;
                return player;
            }
        } else this.anger = 0;
        return null;
    }

    private boolean isPlayerStaring(PlayerEntity player) {
        var helmet = player.getEquippedStack(EquipmentSlot.HEAD);

        if (helmet != null && helmet.getItem() == Items.CARVED_PUMPKIN) {
            return false;
        }
        Vec3d vec3d = player.getRotationVec(1.0F).normalize();
        Vec3d vec3d2 = new Vec3d(this.getX() - player.getX(), this.getEyeY() - player.getEyeY(), this.getZ() - player.getZ());
        double d = vec3d2.length();
        vec3d2 = vec3d2.normalize();
        double e = vec3d.dotProduct(vec3d2);
        return e > 1.0 - 0.025 / d && player.canSee(this);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (CONFIG_COMMON.diamond_chickens.enabled) {
            setDiamondChicken(nbt.getBoolean("DiamondChicken"));
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (CONFIG_COMMON.diamond_chickens.enabled) {
            nbt.putBoolean("DiamondChicken", isDiamondChicken());
        }
    }
}
