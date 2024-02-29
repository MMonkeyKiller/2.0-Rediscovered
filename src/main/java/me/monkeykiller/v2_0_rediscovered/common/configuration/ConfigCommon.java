package me.monkeykiller.v2_0_rediscovered.common.configuration;

import com.electronwill.nightconfig.core.conversion.ObjectConverter;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.file.FileNotFoundAction;
import lombok.Getter;
import lombok.NonNull;
import me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.util.Objects;

import static me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered.LOGGER;

public class ConfigCommon {
    private static transient final @NonNull String fileName = V2_0_Rediscovered.MOD_ID + "-common.toml";

    private static transient final @NonNull ObjectConverter converter = new ObjectConverter(false, false);

    private static transient final @NonNull FileConfig fileConfig = FileConfig
            .builder(getConfigFilePath())
            .onFileNotFound(FileNotFoundAction.copyData(Objects.requireNonNull(V2_0_Rediscovered.class.getClassLoader().getResource(fileName))))
            .preserveInsertionOrder()
            .sync().build();

    @Getter
    private static transient final @NonNull ConfigCommon config = loadConfig();

    // Config

    public EthoSlabConfig etho_slab;
    public ModFeatureConfig burnt_out_torches;
    public PinkWitherConfig pink_wither;
    public ModFeatureConfig horses_and_ponies;
    public RedstoneBugConfig redstone_bugs;
    public DiamondChickensConfig diamond_chickens;
    public ModFeatureConfig floppers;
    public ModFeatureConfig furnace_heat;
    public NeutralChickenConfig neutral_chickens;
    public ModFeatureConfig mob_fatness;
    public ModFeatureConfig flying_sheeps;
    public ModFeatureConfig pigman_battle_signs;
    public SuperHostileModeConfig super_hostile_mode;
    public SpeechBubblesConfig speech_bubbles;
    public ModFeatureConfig tinted_glass;

    //

    private static @NonNull File getConfigFilePath() {
        var configDir = FabricLoader.getInstance().getConfigDir();
        return configDir.resolve(fileName).toFile();
    }

    private static @NonNull ConfigCommon loadConfig() {
        LOGGER.info("Loading config...");
        fileConfig.load();

        var config = converter.toObject(fileConfig, ConfigCommon::new);
        if (!fileConfig.getFile().isFile()) {
            fileConfig.save();
        }
        return config;
    }
}
