package me.monkeykiller.v2_0_rediscovered.common.redstone_bug;

import net.minecraft.entity.data.DataTracker;

public interface RedstoneBugAccessor {
    void setupDataTracker(DataTracker.Builder builder);

    int getColor();

    void setColor(int color);
}
