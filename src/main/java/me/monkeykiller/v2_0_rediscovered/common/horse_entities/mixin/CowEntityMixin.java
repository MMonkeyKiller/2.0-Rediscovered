package me.monkeykiller.v2_0_rediscovered.common.horse_entities.mixin;

import me.monkeykiller.v2_0_rediscovered.common.horse_entities.HorseEntityAccessor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CowEntity.class)
public abstract class CowEntityMixin extends MobEntity implements HorseEntityAccessor {
    private static final TrackedData<Boolean> IS_HORSE = DataTracker.registerData(CowEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    protected CowEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void setupDataTracker() {
        var self = (CowEntity) (Object) this;
        self.getDataTracker().startTracking(IS_HORSE, false);
    }

    public void setupHorse() {
        var self = (CowEntity) (Object) this;
        var world = self.getWorld();
        if (world == null || world.isClient) return;
        if (self.getRandom().nextFloat() >= 0.5D) {
            setHorse(true);
            this.goalSelector.add(3, new TemptGoal(self, 1.25F, Ingredient.ofItems(Items.APPLE, Items.SUGAR), false));
        } else {
            setHorse(false);
            this.goalSelector.add(3, new TemptGoal(self, 1.25F, Ingredient.ofItems(Items.WHEAT), false));
        }
    }

    @Override
    public boolean isHorse() {
        return this.getDataTracker().get(IS_HORSE);
    }

    @Override
    public void setHorse(boolean isHorse) {
        this.getDataTracker().set(IS_HORSE, isHorse);
    }

    @Redirect(method = "initGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/goal/GoalSelector;add(ILnet/minecraft/entity/ai/goal/Goal;)V"))
    public void add(GoalSelector instance, int priority, Goal goal) {
        if (instance == null) return;
        if (!(goal instanceof TemptGoal)) {
            instance.add(priority, goal);
        }
    }

}
