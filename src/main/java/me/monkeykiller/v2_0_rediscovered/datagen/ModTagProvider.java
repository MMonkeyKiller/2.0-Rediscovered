package me.monkeykiller.v2_0_rediscovered.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

import static me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered.*;

public class ModTagProvider extends FabricTagProvider.BlockTagProvider {
    public ModTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).add(FLOPPER_BLOCK);
        configureColoredTintedGlassTags();
    }

    private void configureColoredTintedGlassTags() {
        var impermeableTag = getOrCreateTagBuilder(BlockTags.IMPERMEABLE);
        var glassBlocksTag = getOrCreateTagBuilder(ConventionalBlockTags.GLASS_BLOCKS);

        for (var blockItem : TINTED_GLASS_ITEMS.values()) {
            var block = blockItem.getBlock();
            impermeableTag.add(block);
            glassBlocksTag.add(block);
        }
    }
}
