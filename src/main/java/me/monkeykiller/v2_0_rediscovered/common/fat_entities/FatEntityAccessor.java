package me.monkeykiller.v2_0_rediscovered.common.fat_entities;

public interface FatEntityAccessor {
    byte getFatness();
    void setFatness(int value);

    default boolean isFattenable() {
        return false;
    }
}
