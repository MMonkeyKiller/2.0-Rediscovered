package me.monkeykiller.v2_0_rediscovered.common;

import lombok.SneakyThrows;
import me.monkeykiller.v2_0_rediscovered.common.configuration.ConfigUtils;
import me.monkeykiller.v2_0_rediscovered.common.etho_slab.EthoSlabBlock;
import me.monkeykiller.v2_0_rediscovered.common.speech_bubbles.SpeechBubbleEntity;
import me.monkeykiller.v2_0_rediscovered.common.superhostilemode.SuperHostileModeInstance;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class V2_0_Rediscovered implements ModInitializer {
    public static final EntityType<SpeechBubbleEntity> SPEECH_BUBBLE = Registry.register(
            Registries.ENTITY_TYPE, identifier("speech_bubble"),
            FabricEntityTypeBuilder.<SpeechBubbleEntity>create(SpawnGroup.MISC, SpeechBubbleEntity::new)
                    .dimensions(EntityDimensions.fixed(0.2f, 0.2f))
                    .build());

    public static final EthoSlabBlock ETHO_SLAB_BLOCK = new EthoSlabBlock(Settings.of(Material.TNT, MapColor.RED).strength(2.0F, 10.0F).sounds(BlockSoundGroup.GRASS));
    public static final BlockItem ETHO_SLAB_ITEM = new BlockItem(ETHO_SLAB_BLOCK, new FabricItemSettings());

    public static final String MOD_ID = "v2_0_rediscovered";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static Identifier identifier(@NotNull String path) {
        return new Identifier(MOD_ID, path);
    }

    @SneakyThrows
    @Override
    public void onInitialize() {
        ConfigUtils.load();

        Registry.register(Registries.BLOCK, identifier("etho_slab"), ETHO_SLAB_BLOCK);
        Registry.register(Registries.ITEM, identifier("etho_slab"), ETHO_SLAB_ITEM);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(content -> {
            content.add(ETHO_SLAB_ITEM);
        });

        if (ConfigUtils.isSuperHostileModeEnabled()) {
            ServerTickEvents.END_WORLD_TICK.register(world -> {
                SuperHostileModeInstance.get(world).tick();
            });
        }

    }
}
