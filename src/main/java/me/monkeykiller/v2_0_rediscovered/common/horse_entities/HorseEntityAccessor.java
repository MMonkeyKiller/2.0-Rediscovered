package me.monkeykiller.v2_0_rediscovered.common.horse_entities;

import net.minecraft.entity.data.DataTracker;

public interface HorseEntityAccessor {
    void setupDataTracker(DataTracker.Builder builder);
    boolean isHorse();
    void setHorse(boolean value);
    void setupHorse();
}
