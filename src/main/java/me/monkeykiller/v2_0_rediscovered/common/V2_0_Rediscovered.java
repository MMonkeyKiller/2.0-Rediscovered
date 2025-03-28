package me.monkeykiller.v2_0_rediscovered.common;

import lombok.NonNull;
import lombok.SneakyThrows;
import me.monkeykiller.v2_0_rediscovered.common.configuration.ConfigCommon;
import me.monkeykiller.v2_0_rediscovered.common.etho_slab.EthoSlabBlock;
import me.monkeykiller.v2_0_rediscovered.common.floppers.FlopperBlock;
import me.monkeykiller.v2_0_rediscovered.common.floppers.FlopperBlockEntity;
import me.monkeykiller.v2_0_rediscovered.common.pink_wither.WitherHugEntity;
import me.monkeykiller.v2_0_rediscovered.common.pink_wither.WitherLoveEntity;
import me.monkeykiller.v2_0_rediscovered.common.speech_bubbles.SpeechBubbleEntity;
import me.monkeykiller.v2_0_rediscovered.common.superhostilemode.SuperHostileModeInstance;
import me.monkeykiller.v2_0_rediscovered.common.tinted_glasses.ColoredTintedGlassBlock;
import me.monkeykiller.v2_0_rediscovered.common.torch_off.TorchOffBlock;
import me.monkeykiller.v2_0_rediscovered.common.torch_off.WallTorchOffBlock;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
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

import java.util.HashMap;
import java.util.Map;

public class V2_0_Rediscovered implements ModInitializer {
    public static final String MOD_ID = "v2_0_rediscovered";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final ConfigCommon CONFIG_COMMON = ConfigCommon.getConfig();

    public static final EntityType<SpeechBubbleEntity> SPEECH_BUBBLE = register("speech_bubble", EntityType.Builder
            .<SpeechBubbleEntity>create(SpeechBubbleEntity::new, SpawnGroup.MISC)
            .dimensions(0.2f, 0.2f));

    public static final EntityType<WitherHugEntity> WITHER_HUG = register("wither_hug", EntityType.Builder
            .create(WitherHugEntity::new, SpawnGroup.CREATURE)
            .dimensions(0.9f, 3.5f));

    public static final EntityType<WitherLoveEntity> WITHER_LOVE = register("wither_love", EntityType.Builder
            .<WitherLoveEntity>create(WitherLoveEntity::new, SpawnGroup.MISC)
            .dimensions(0.3125F, 0.3125F));

    public static final Block ETHO_SLAB_BLOCK = register("etho_slab", new EthoSlabBlock(Settings.copy(Blocks.TNT).strength(2.0F, 10.0F).sounds(BlockSoundGroup.GRASS)));
    public static final Item ETHO_SLAB_ITEM = register("etho_slab", new BlockItem(ETHO_SLAB_BLOCK, new Item.Settings()));

    public static final Block TORCH_OFF_BLOCK = register("torch_off", new TorchOffBlock(Settings.copy(Blocks.TORCH).luminance(state -> 0)));
    public static final Block WALL_TORCH_OFF_BLOCK = register("wall_torch_off", new WallTorchOffBlock(Settings.copy(Blocks.WALL_TORCH).dropsLike(TORCH_OFF_BLOCK).luminance(state -> 0)));
    public static final Item TORCH_OFF_ITEM = register("torch_off", new VerticallyAttachableBlockItem(TORCH_OFF_BLOCK, WALL_TORCH_OFF_BLOCK, new Item.Settings(), Direction.DOWN));

    public static final Block FLOPPER_BLOCK = register("flopper", new FlopperBlock(Settings.copy(Blocks.DROPPER)));
    public static final Item FLOPPER_ITEM = register("flopper", new BlockItem(FLOPPER_BLOCK, new Item.Settings()));

    public static final Map<Identifier, BlockItem> TINTED_GLASS_ITEMS = new HashMap<>();

    public static final Block WHITE_TINTED_GLASS_BLOCK = registerTintedGlass("white_tinted_glass", 0xFFFFFF, MapColor.WHITE);
    public static final Block ORANGE_TINTED_GLASS_BLOCK = registerTintedGlass("orange_tinted_glass", 0xD87F33, MapColor.ORANGE);
    public static final Block MAGENTA_TINTED_GLASS_BLOCK = registerTintedGlass("magenta_tinted_glass", 0xB24CD8, MapColor.MAGENTA);
    public static final Block LIGHT_BLUE_TINTED_GLASS_BLOCK = registerTintedGlass("light_blue_tinted_glass", 0x6699D8, MapColor.LIGHT_BLUE);
    public static final Block YELLOW_TINTED_GLASS_BLOCK = registerTintedGlass("yellow_tinted_glass", 0xE5E533, MapColor.YELLOW);
    public static final Block LIME_TINTED_GLASS_BLOCK = registerTintedGlass("lime_tinted_glass", 0x7FCC19, MapColor.LIME);
    public static final Block PINK_TINTED_GLASS_BLOCK = registerTintedGlass("pink_tinted_glass", 0xF27FA5, MapColor.PINK);
    public static final Block GRAY_TINTED_GLASS_BLOCK = registerTintedGlass("gray_tinted_glass", 0x4C4C4C, MapColor.GRAY);
    public static final Block SILVER_TINTED_GLASS_BLOCK = registerTintedGlass("silver_tinted_glass", 0x999999, MapColor.STONE_GRAY);
    public static final Block CYAN_TINTED_GLASS_BLOCK = registerTintedGlass("cyan_tinted_glass", 0x4C7F99, MapColor.CYAN);
    public static final Block PURPLE_TINTED_GLASS_BLOCK = registerTintedGlass("purple_tinted_glass", 0x7F3FB2, MapColor.PURPLE);
    public static final Block BLUE_TINTED_GLASS_BLOCK = registerTintedGlass("blue_tinted_glass", 0x334CB2, MapColor.BLUE);
    public static final Block BROWN_TINTED_GLASS_BLOCK = registerTintedGlass("brown_tinted_glass", 0x664C33, MapColor.BROWN);
    public static final Block GREEN_TINTED_GLASS_BLOCK = registerTintedGlass("green_tinted_glass", 0x667F33, MapColor.GREEN);
    public static final Block RED_TINTED_GLASS_BLOCK = registerTintedGlass("red_tinted_glass", 0x993333, MapColor.RED);
    public static final Block BLACK_TINTED_GLASS_BLOCK = registerTintedGlass("black_tinted_glass", 0x191919, MapColor.BLACK);

    public static final BlockEntityType<FlopperBlockEntity> FLOPPER_BLOCK_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE, identifier("flopper_block_entity"),
            BlockEntityType.Builder.create(FlopperBlockEntity::new, FLOPPER_BLOCK).build(null)
    );

    public static Identifier identifier(@NotNull String path) {
        return Identifier.of(MOD_ID, path);
    }

    @SneakyThrows
    @Override
    public void onInitialize() {
        FabricDefaultAttributeRegistry.register(WITHER_HUG, WitherHugEntity.createWitherHugAttributes());

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(content -> {
            if (CONFIG_COMMON.etho_slab.enabled) content.add(ETHO_SLAB_ITEM);
            if (CONFIG_COMMON.burnt_out_torches.enabled) content.add(TORCH_OFF_ITEM);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(content -> {
            if (CONFIG_COMMON.floppers.enabled) content.add(FLOPPER_ITEM);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COLORED_BLOCKS).register(content -> {
            if (CONFIG_COMMON.tinted_glass.enabled) TINTED_GLASS_ITEMS.values().forEach(content::add);
        });

        if (CONFIG_COMMON.super_hostile_mode.enabled) {
            ServerTickEvents.END_WORLD_TICK.register(world -> {
                SuperHostileModeInstance.get(world).tick();
            });
        }
    }

    //

    private static <T extends Block> T register(@NonNull String id, @NonNull T block) {
        return Registry.register(Registries.BLOCK, identifier(id), block);
    }

    private static Block registerTintedGlass(@NonNull String id, int color, @NonNull MapColor mapColor) {
        var settings = AbstractBlock.Settings.copyShallow(Blocks.TINTED_GLASS).mapColor(mapColor);
        var block = register(id, new ColoredTintedGlassBlock(settings, color));
        TINTED_GLASS_ITEMS.put(identifier(id), register(id, new BlockItem(block, new Item.Settings())));
        return block;
    }

    private static <T extends Item> T register(@NonNull String id, @NonNull T item) {
        return Registry.register(Registries.ITEM, identifier(id), item);
    }

    private static <T extends Entity> EntityType<T> register(@NonNull String id, @NonNull EntityType.Builder<T> builder) {
        var identifier = identifier(id);
        return Registry.register(Registries.ENTITY_TYPE, identifier, builder.build(identifier.toString()));
    }
}
