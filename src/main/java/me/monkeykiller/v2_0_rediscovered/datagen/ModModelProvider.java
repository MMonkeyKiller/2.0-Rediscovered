package me.monkeykiller.v2_0_rediscovered.datagen;

import me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

import java.util.Optional;

import static me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered.*;

public class ModModelProvider extends FabricModelProvider {
    private final Model COLORED_TINTED_GLASS = new Model(Optional.of(V2_0_Rediscovered.identifier("block/colored_tinted_glass")), Optional.empty());

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        registerEthoSlabModel(blockStateModelGenerator);
        blockStateModelGenerator.registerTorch(TORCH_OFF_BLOCK, WALL_TORCH_OFF_BLOCK);
        registerFlopper(blockStateModelGenerator);

        for (var tintedGlass : TINTED_GLASS_ITEMS.values()) {
            blockStateModelGenerator.registerSingleton(tintedGlass.getBlock(), new TextureMap(), COLORED_TINTED_GLASS);
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
    }

    public final void registerFlopper(BlockStateModelGenerator blockStateModelGenerator) {
        var modelCollector = blockStateModelGenerator.modelCollector;
        var textureMap = new TextureMap()
                .put(TextureKey.TOP, TextureMap.getSubId(Blocks.FURNACE, "_top"))
                .put(TextureKey.SIDE, TextureMap.getSubId(Blocks.FURNACE, "_side"))
                .put(TextureKey.FRONT, TextureMap.getSubId(Blocks.DROPPER, "_front"));
        var textureMapVertical = new TextureMap()
                .put(TextureKey.SIDE, TextureMap.getSubId(Blocks.FURNACE, "_top"))
                .put(TextureKey.FRONT, TextureMap.getSubId(Blocks.DROPPER, "_front_vertical"));

        var identifier = Models.ORIENTABLE.upload(FLOPPER_BLOCK, textureMap, modelCollector);
        var identifierVertical = Models.ORIENTABLE_VERTICAL.upload(FLOPPER_BLOCK, textureMapVertical, modelCollector);
        blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(FLOPPER_BLOCK).coordinate(BlockStateVariantMap.create(Properties.FACING).register(Direction.DOWN, BlockStateVariant.create().put(VariantSettings.MODEL, identifierVertical).put(VariantSettings.X, VariantSettings.Rotation.R180)).register(Direction.UP, BlockStateVariant.create().put(VariantSettings.MODEL, identifierVertical)).register(Direction.NORTH, BlockStateVariant.create().put(VariantSettings.MODEL, identifier)).register(Direction.EAST, BlockStateVariant.create().put(VariantSettings.MODEL, identifier).put(VariantSettings.Y, VariantSettings.Rotation.R90)).register(Direction.SOUTH, BlockStateVariant.create().put(VariantSettings.MODEL, identifier).put(VariantSettings.Y, VariantSettings.Rotation.R180)).register(Direction.WEST, BlockStateVariant.create().put(VariantSettings.MODEL, identifier).put(VariantSettings.Y, VariantSettings.Rotation.R270))));
    }

    private void registerEthoSlabModel(BlockStateModelGenerator blockStateModelGenerator) {
        var modelCollector = blockStateModelGenerator.modelCollector;
        var tntIdentifier = ModelIds.getBlockModelId(Blocks.TNT);
        var tntTextures = TexturedModel.CUBE_BOTTOM_TOP.get(Blocks.TNT).getTextures();

        var slabIdentifier = Models.SLAB.upload(ETHO_SLAB_BLOCK, tntTextures, modelCollector);
        var slabTopIdentifier = Models.SLAB_TOP.upload(ETHO_SLAB_BLOCK, tntTextures, modelCollector);
        blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createSlabBlockState(ETHO_SLAB_BLOCK, slabIdentifier, slabTopIdentifier, tntIdentifier));
    }
}
