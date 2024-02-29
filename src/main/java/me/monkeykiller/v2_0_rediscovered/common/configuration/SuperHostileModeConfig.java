package me.monkeykiller.v2_0_rediscovered.common.configuration;

public class SuperHostileModeConfig extends ModFeatureConfig {
    public int next_spawn_wave_cooldown_min, next_spawn_wave_cooldown_max;
    public int spawn_wave_duration_min, spawn_wave_duration_max;
    public int spawn_wave_attempts_min, spawn_wave_attempts_max;

    public boolean apply_blindness;
    public boolean spawn_fake_lightning;
    public boolean only_activate_on_survival;
}
