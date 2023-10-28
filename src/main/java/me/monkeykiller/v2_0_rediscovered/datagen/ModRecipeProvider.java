package me.monkeykiller.v2_0_rediscovered.datagen;

import me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, V2_0_Rediscovered.ETHO_SLAB_ITEM, 6)
                .pattern("###")
                .input('#', Blocks.TNT)
                .criterion(hasItem(Items.TNT), conditionsFromItem(Items.TNT))
                .showNotification(true)
                .offerTo(exporter, V2_0_Rediscovered.identifier(getRecipeName(V2_0_Rediscovered.ETHO_SLAB_ITEM)));
    }
}
