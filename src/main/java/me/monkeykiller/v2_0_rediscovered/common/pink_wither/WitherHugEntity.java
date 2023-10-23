package me.monkeykiller.v2_0_rediscovered.common.pink_wither;

import net.minecraft.client.render.entity.feature.SkinOverlayOwner;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.jetbrains.annotations.Nullable;

public class WitherHugEntity extends HostileEntity implements SkinOverlayOwner, RangedAttackMob {

    private static final TrackedData<Integer> INVUL_TIMER = DataTracker.registerData(WitherHugEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private static final TrackedData<Integer> HEADS = DataTracker.registerData(WitherHugEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private int ticksUntilNextSkull = 600;

    public WitherHugEntity(EntityType<? extends WitherHugEntity> entityType, World world) {
        super(entityType, world);
        this.setHealth(this.getMaxHealth());
        this.setInvulTimer(1);
        this.experiencePoints = 50;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new TemptGoal(this, 1.66d, Ingredient.ofItems(Items.CAKE, Items.SUGAR), false));
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0d));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(7, new LookAroundGoal(this));
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(INVUL_TIMER, 0);
        this.dataTracker.startTracking(HEADS, 0);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Invul", this.getInvulnerableTimer());
        nbt.putInt("Heads", this.getHeads());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setInvulTimer(nbt.getInt("Invul"));
        this.setHeads(nbt.getInt("Heads"));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_WITHER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_WITHER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_WITHER_DEATH;
    }

    @Override
    public void tickMovement() {
        var vec3d = this.getVelocity();
        if (vec3d.horizontalLengthSquared() > 0.05) {
            this.setYaw((float) MathHelper.atan2(vec3d.z, vec3d.x) * 57.295776f - 90.0f);
        }
        super.tickMovement();

        boolean renderOverlay = this.shouldRenderOverlay();
        for (int i = 0; i < 3; i++) {
            var headX = this.getHeadX(i);
            var headY = this.getHeadY(i);
            var headZ = this.getHeadZ(i);

            if (canSpawnHeartParticles(i)) {
                this.getWorld().addParticle(ParticleTypes.HEART,
                        headX + this.random.nextGaussian() * 0.30000001192092896D,
                        headY + this.random.nextGaussian() * 0.30000001192092896D,
                        headZ + this.random.nextGaussian() * 0.30000001192092896D,
                        0.0D, 0.0D, 0.0D);
                if (renderOverlay && this.getWorld().random.nextInt(4) == 0) {
                    this.getWorld().addParticle(ParticleTypes.ENTITY_EFFECT, headX + this.random.nextGaussian() * (double) 0.3f, headY + this.random.nextGaussian() * (double) 0.3f, headZ + this.random.nextGaussian() * (double) 0.3f, 0.7f, 0.7f, 0.5);
                }
            }
        }

        if (this.getInvulnerableTimer() > 0) {
            for (int i = 0; i < 3; i++) {
                this.getWorld().addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + this.random.nextGaussian(), this.getY() + (double) (this.random.nextFloat() * 3.3f), this.getZ() + this.random.nextGaussian(), 0.7f, 0.7f, 0.9f);
            }
        }
    }

    private boolean canSpawnHeartParticles(int i) {
        return switch (i) {
            default -> true;
            case 1 -> this.getHeads() >= 2;
            case 2 -> this.getHeads() >= 1;
        };
    }

    @Override
    protected void mobTick() {
        if (this.getInvulnerableTimer() <= 0) {
            if (this.ticksUntilNextSkull-- <= 0) {
                float radiusXZ = 10.0F;
                float radiusY = 5.0F;
                double x = MathHelper.nextDouble(this.random, this.getX() - radiusXZ, this.getX() + radiusXZ);
                double y = MathHelper.nextDouble(this.random, this.getY() - radiusY, this.getY() + radiusY);
                double z = MathHelper.nextDouble(this.random, this.getZ() - radiusXZ, this.getZ() + radiusXZ);
                this.shootSkullAt(x, y, z);
                this.ticksUntilNextSkull = MathHelper.nextInt(this.random, 600, 1400);
            }

            super.mobTick();

            if (this.age % 20 == 0) {
                this.heal(1);
            }

            return;
        }

        int newInvulnerableTimer = this.getInvulnerableTimer() - 1;
        if (newInvulnerableTimer <= 0) {
            for (int x = -7; x <= 7; ++x) {
                for (int z = -7; z <= 7; ++z) {
                    for (int y = -1; y <= 3; ++y) {
                        var blockPos = this.getBlockPos().add(x, y, z);
                        if (BoneMealItem.useOnFertilizable(new ItemStack(Items.AIR), this.getWorld(), blockPos) && !this.getWorld().isClient()) {
                            this.getWorld().syncWorldEvent(WorldEvents.BONE_MEAL_USED, blockPos, 0);
                        }
                    }
                }
            }
        }

        this.setInvulTimer(newInvulnerableTimer);

        if (this.age % 10 == 0) {
            this.heal(10);
        }
    }

    private double getHeadX(int headIndex) {
        if (headIndex <= 0) {
            return this.getX();
        }
        float f = (this.bodyYaw + (float) (180 * (headIndex - 1))) * ((float) Math.PI / 180);
        float g = MathHelper.cos(f);
        return this.getX() + (double) g * 1.3;
    }

    private double getHeadY(int headIndex) {
        if (headIndex <= 0) {
            return this.getY() + 3.0;
        }
        return this.getY() + 2.2;
    }

    private double getHeadZ(int headIndex) {
        if (headIndex <= 0) {
            return this.getZ();
        }
        float f = (this.bodyYaw + (float) (180 * (headIndex - 1))) * ((float) Math.PI / 180);
        float g = MathHelper.sin(f);
        return this.getZ() + (double) g * 1.3;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        Entity entity;
        if (this.isInvulnerableTo(source)) {
            return false;
        }
        if (source.isIn(DamageTypeTags.WITHER_IMMUNE_TO) || source.getAttacker() instanceof WitherEntity) {
            return false;
        }
        if (this.getInvulnerableTimer() > 0 && !source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        }
        if (this.shouldRenderOverlay() && source.getSource() instanceof PersistentProjectileEntity) {
            return false;
        }
        entity = source.getAttacker();
        if (!(entity instanceof PlayerEntity) && entity instanceof LivingEntity && ((LivingEntity) entity).getGroup() == this.getGroup()) {
            return false;
        }

        return super.damage(source, amount);
    }

    @Override
    protected void dropEquipment(DamageSource source, int lootingMultiplier, boolean allowDrops) {
        super.dropEquipment(source, lootingMultiplier, allowDrops);
        ItemEntity itemEntity = this.dropItem(Items.NETHER_STAR);
        if (itemEntity != null) {
            itemEntity.setCovetedItem();
        }
    }

    @Override
    public void checkDespawn() {
        if (this.getWorld().getDifficulty() == Difficulty.PEACEFUL && this.isDisallowedInPeaceful()) {
            this.discard();
            return;
        }
        this.despawnCounter = 0;
    }

    @Override
    public boolean addStatusEffect(StatusEffectInstance effect, @Nullable Entity source) {
        return false;
    }

    public static DefaultAttributeContainer.Builder createWitherHugAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 300.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3f)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 40.0)
                .add(EntityAttributes.GENERIC_ARMOR, 4.0);
    }

    public int getInvulnerableTimer() {
        return this.dataTracker.get(INVUL_TIMER);
    }

    public void setInvulTimer(int ticks) {
        this.dataTracker.set(INVUL_TIMER, ticks);
    }

    @Override
    public boolean shouldRenderOverlay() {
        return this.getHealth() <= this.getMaxHealth() / 2.0f;
    }

    @Override
    public EntityGroup getGroup() {
        return EntityGroup.UNDEAD;
    }

    @Override
    protected boolean canStartRiding(Entity entity) {
        return false;
    }

    @Override
    public boolean canUsePortals() {
        return false;
    }

    @Override
    public boolean canHaveStatusEffect(StatusEffectInstance effect) {
        if (effect.getEffectType() == StatusEffects.WITHER) {
            return false;
        }
        return super.canHaveStatusEffect(effect);
    }

    public int getHeads() {
        return this.dataTracker.get(HEADS);
    }

    public void setHeads(int heads) {
        this.dataTracker.set(HEADS, heads);
    }

    private void shootSkullAt(LivingEntity target) {
        this.shootSkullAt(target.getX(), target.getY() + (double) target.getStandingEyeHeight() * 0.5, target.getZ());
    }

    private void shootSkullAt(double targetX, double targetY, double targetZ) {
        if (!this.isSilent()) {
            this.getWorld().syncWorldEvent(null, WorldEvents.WITHER_SHOOTS, this.getBlockPos(), 0);
        }
        var targetPos = new Vec3d(targetX, targetY, targetZ);
        var headPos = new Vec3d(this.getHeadX(0), this.getHeadY(0), this.getHeadZ(0));
        var dir = targetPos.subtract(headPos);

        var witherLoveEntity = new WitherLoveEntity(
                this.getWorld(), this,
                dir.getX(), dir.getY(), dir.getZ());
        witherLoveEntity.setOwner(this);
        witherLoveEntity.setPos(headPos.getX(), headPos.getY(), headPos.getZ());
        this.getWorld().spawnEntity(witherLoveEntity);
    }

    @Override
    public void shootAt(LivingEntity target, float pullProgress) {
        this.shootSkullAt(target);
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        var itemStack = player.getStackInHand(hand);
        if (itemStack != null && itemStack.isOf(Items.SUGAR)) {
            if (!player.isCreative()) {
                itemStack.decrement(1);
            }
            this.setHeads(this.getHeads() + 1);
            return ActionResult.success(false);
        }
        return super.interactMob(player, hand);
    }

    @Override
    public float getSoundPitch() {
        return 6.5f;
    }
}
