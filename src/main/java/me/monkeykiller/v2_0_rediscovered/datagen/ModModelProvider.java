package me.monkeykiller.v2_0_rediscovered.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.*;

import static me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered.*;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        registerEthoSlabModel(blockStateModelGenerator);
        blockStateModelGenerator.registerTorch(TORCH_OFF_BLOCK, WALL_TORCH_OFF_BLOCK);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
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
