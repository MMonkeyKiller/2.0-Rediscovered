package me.monkeykiller.v2_0_rediscovered.common.redstone_bug.mixin;

import me.monkeykiller.v2_0_rediscovered.common.redstone_bug.RedstoneBugAccessor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SilverfishEntity.class)
public class SilverfishEntityMixin extends MobEntity implements RedstoneBugAccessor {
    @Unique
    private static final TrackedData<Integer> COLOR = DataTracker.registerData(SilverfishEntity.class, TrackedDataHandlerRegistry.INTEGER);

    protected SilverfishEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void setupDataTracker() {
        getDataTracker().startTracking(COLOR, 0);
    }

    @Override
    public int getColor() {
        return getDataTracker().get(COLOR);
    }

    @Override
    public void setColor(int color) {
        getDataTracker().set(COLOR, color);
    }
}
