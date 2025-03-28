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
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PigEntity.class)
public abstract class PigEntityMixin extends MobEntity implements HorseEntityAccessor {
    private static final TrackedData<Boolean> IS_HORSE = DataTracker.registerData(PigEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    protected PigEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void setupDataTracker(DataTracker.Builder builder) {
        builder.add(IS_HORSE, false);
    }

    public void setupHorse() {
        var self = (PigEntity) (Object) this;
        var world = self.getWorld();
        if (world == null || world.isClient) return;
        if (self.getRandom().nextFloat() >= 0.5D) {
            setHorse(true);
            this.goalSelector.add(3, new TemptGoal(self, 1.25F, Ingredient.ofItems(Items.APPLE, Items.SUGAR), false));
        } else {
            setHorse(false);
            this.goalSelector.add(3, new TemptGoal(self, 1.25F, Ingredient.ofItems(Items.CARROT), false));
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
