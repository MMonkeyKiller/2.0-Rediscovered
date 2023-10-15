package me.monkeykiller.v2_0_rediscovered.common.configuration;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@UtilityClass
public class ConfigUtils {
    private final SimpleConfig config = SimpleConfig.of(V2_0_Rediscovered.MOD_ID)
            .provider(ConfigUtils::getDefaultConfig).request();

    //

    /*         Super Hostile Mode         */
    @Getter
    private boolean superHostileModeEnabled;
    @Getter
    private int superHostileModeNextSpawnWaveCooldownMin;
    @Getter
    private int superHostileModeNextSpawnWaveCooldownMax;
    @Getter
    private int superHostileModeSpawnWaveDurationMin;
    @Getter
    private int superHostileModeSpawnWaveDurationMax;
    @Getter
    private int superHostileModeSpawnWaveAttemptsMin;
    @Getter
    private int superHostileModeSpawnWaveAttemptsMax;
    @Getter
    private boolean superHostileModeApplyBlindness;
    @Getter
    private boolean superHostileModeSpawnFakeLightning;
    @Getter
    private boolean onlyActivateOnSurvival;

    public void load() {
        final var logger = V2_0_Rediscovered.LOGGER;

        // Super Hostile Mode
        superHostileModeEnabled = config.getOrDefault("super_hostile_mode.enabled", false);
        superHostileModeNextSpawnWaveCooldownMin = config.getOrDefault("super_hostile_mode.next_spawn_wave.cooldown.min", 40);
        superHostileModeNextSpawnWaveCooldownMax = config.getOrDefault("super_hostile_mode.next_spawn_wave.cooldown.max", 90);
        superHostileModeSpawnWaveDurationMin = config.getOrDefault("super_hostile_mode.spawn_wave.duration.min", 15);
        superHostileModeSpawnWaveDurationMax = config.getOrDefault("super_hostile_mode.spawn_wave.duration.max", 30);
        superHostileModeSpawnWaveAttemptsMin = config.getOrDefault("super_hostile_mode.spawn_wave.attempts.min", 100);
        superHostileModeSpawnWaveAttemptsMax = config.getOrDefault("super_hostile_mode.spawn_wave.attempts.max", 200);
        superHostileModeApplyBlindness = config.getOrDefault("super_hostile_mode.apply_blindness", false);
        superHostileModeSpawnFakeLightning = config.getOrDefault("super_hostile_mode.spawn_fake_lightning", false);
        onlyActivateOnSurvival = config.getOrDefault("super_hostile_mode.only_activate_on_survival", true);

        //

        logger.info("Config loaded!");
        logger.info("Super Hostile Mode = " + superHostileModeEnabled);
    }

    //

    private String getDefaultConfig(String filename) {
        try (var resource = V2_0_Rediscovered.class.getClassLoader().getResourceAsStream(filename + ".properties")) {
            if (resource == null) return null;
            try (var streamReader = new InputStreamReader(resource)) {
                var reader = new BufferedReader(streamReader);
                return reader.lines().collect(Collectors.joining("\n")) + "\n";
            }
        } catch (Exception e) {
            return "# Error reading resource:" + e.getMessage();
        }
    }
}
