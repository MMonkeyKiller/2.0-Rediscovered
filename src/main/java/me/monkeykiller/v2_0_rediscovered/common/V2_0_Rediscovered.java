package me.monkeykiller.v2_0_rediscovered.common;

import lombok.NonNull;
import lombok.SneakyThrows;
import me.monkeykiller.v2_0_rediscovered.common.configuration.ConfigUtils;
import me.monkeykiller.v2_0_rediscovered.common.etho_slab.EthoSlabBlock;
import me.monkeykiller.v2_0_rediscovered.common.pink_wither.WitherHugEntity;
import me.monkeykiller.v2_0_rediscovered.common.pink_wither.WitherLoveEntity;
import me.monkeykiller.v2_0_rediscovered.common.speech_bubbles.SpeechBubbleEntity;
import me.monkeykiller.v2_0_rediscovered.common.superhostilemode.SuperHostileModeInstance;
import me.monkeykiller.v2_0_rediscovered.common.torch_off.TorchOffBlock;
import me.monkeykiller.v2_0_rediscovered.common.torch_off.WallTorchOffBlock;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.VerticallyAttachableBlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class V2_0_Rediscovered implements ModInitializer {
    public static final String MOD_ID = "v2_0_rediscovered";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final EntityType<SpeechBubbleEntity> SPEECH_BUBBLE = Registry.register(
            Registries.ENTITY_TYPE, identifier("speech_bubble"),
            FabricEntityTypeBuilder.<SpeechBubbleEntity>create(SpawnGroup.MISC, SpeechBubbleEntity::new)
                    .dimensions(EntityDimensions.fixed(0.2f, 0.2f))
                    .build());

    public static final EntityType<WitherHugEntity> WITHER_HUG = Registry.register(
            Registries.ENTITY_TYPE, identifier("wither_hug"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, WitherHugEntity::new)
                    .dimensions(EntityDimensions.fixed(0.9f, 3.5f))
                    .build());

    public static final EntityType<WitherLoveEntity> WITHER_LOVE = Registry.register(
            Registries.ENTITY_TYPE, identifier("wither_love"),
            FabricEntityTypeBuilder.<WitherLoveEntity>create(SpawnGroup.MISC, WitherLoveEntity::new)
                    .dimensions(EntityDimensions.fixed(0.3125F, 0.3125F))
                    .build());

    public static final Block ETHO_SLAB_BLOCK = register("etho_slab", new EthoSlabBlock(Settings.copy(Blocks.TNT).strength(2.0F, 10.0F).sounds(BlockSoundGroup.GRASS)));
    public static final Item ETHO_SLAB_ITEM = register("etho_slab", new BlockItem(ETHO_SLAB_BLOCK, new FabricItemSettings()));

    public static final Block TORCH_OFF_BLOCK = register("torch_off", new TorchOffBlock(Settings.copy(Blocks.TORCH).luminance(state -> 0)));
    public static final Block WALL_TORCH_OFF_BLOCK = register("wall_torch_off", new WallTorchOffBlock(Settings.copy(Blocks.WALL_TORCH).dropsLike(TORCH_OFF_BLOCK).luminance(state -> 0)));
    public static final Item TORCH_OFF_ITEM = register("torch_off", new VerticallyAttachableBlockItem(TORCH_OFF_BLOCK, WALL_TORCH_OFF_BLOCK, new FabricItemSettings(), Direction.DOWN));

    public static Identifier identifier(@NotNull String path) {
        return new Identifier(MOD_ID, path);
    }

    @SneakyThrows
    @Override
    public void onInitialize() {
        ConfigUtils.load();

        FabricDefaultAttributeRegistry.register(WITHER_HUG, WitherHugEntity.createWitherHugAttributes());

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(content -> {
            content.add(ETHO_SLAB_ITEM);
            content.add(TORCH_OFF_ITEM);
        });

        if (ConfigUtils.isSuperHostileModeEnabled()) {
            ServerTickEvents.END_WORLD_TICK.register(world -> {
                SuperHostileModeInstance.get(world).tick();
            });
        }
    }

    //

    private static Block register(@NonNull String id, @NonNull Block block) {
        return Registry.register(Registries.BLOCK, identifier(id), block);
    }

    private static Item register(@NonNull String id, @NonNull Item item) {
        return Registry.register(Registries.ITEM, identifier(id), item);
    }
}
