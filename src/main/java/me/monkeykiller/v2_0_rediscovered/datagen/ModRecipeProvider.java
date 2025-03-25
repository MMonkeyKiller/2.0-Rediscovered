package me.monkeykiller.v2_0_rediscovered.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

import static me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered.*;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ETHO_SLAB_ITEM, 6)
                .pattern("###")
                .input('#', Blocks.TNT)
                .criterion(hasItem(Items.TNT), conditionsFromItem(Items.TNT))
                .showNotification(true)
                .offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.REDSTONE, FLOPPER_BLOCK)
                .input(Blocks.DROPPER)
                .criterion(hasItem(Items.DROPPER), conditionsFromItem(Items.DROPPER))
                .offerTo(exporter, identifier("dropper_to_flopper"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.REDSTONE, Blocks.DROPPER)
                .input(FLOPPER_BLOCK)
                .criterion(hasItem(FLOPPER_ITEM), conditionsFromItem(FLOPPER_ITEM))
                .offerTo(exporter, identifier("flopper_to_dropper"));

        offerTintedGlassDyeingRecipe(exporter, WHITE_TINTED_GLASS_BLOCK, Items.WHITE_DYE);
        offerTintedGlassDyeingRecipe(exporter, ORANGE_TINTED_GLASS_BLOCK, Items.ORANGE_DYE);
        offerTintedGlassDyeingRecipe(exporter, MAGENTA_TINTED_GLASS_BLOCK, Items.MAGENTA_DYE);
        offerTintedGlassDyeingRecipe(exporter, LIGHT_BLUE_TINTED_GLASS_BLOCK, Items.LIGHT_BLUE_DYE);
        offerTintedGlassDyeingRecipe(exporter, YELLOW_TINTED_GLASS_BLOCK, Items.YELLOW_DYE);
        offerTintedGlassDyeingRecipe(exporter, LIME_TINTED_GLASS_BLOCK, Items.LIME_DYE);
        offerTintedGlassDyeingRecipe(exporter, PINK_TINTED_GLASS_BLOCK, Items.PINK_DYE);
        offerTintedGlassDyeingRecipe(exporter, GRAY_TINTED_GLASS_BLOCK, Items.GRAY_DYE);
        offerTintedGlassDyeingRecipe(exporter, SILVER_TINTED_GLASS_BLOCK, Items.LIGHT_GRAY_DYE);
        offerTintedGlassDyeingRecipe(exporter, CYAN_TINTED_GLASS_BLOCK, Items.CYAN_DYE);
        offerTintedGlassDyeingRecipe(exporter, PURPLE_TINTED_GLASS_BLOCK, Items.PURPLE_DYE);
        offerTintedGlassDyeingRecipe(exporter, BLUE_TINTED_GLASS_BLOCK, Items.BLUE_DYE);
        offerTintedGlassDyeingRecipe(exporter, BROWN_TINTED_GLASS_BLOCK, Items.BROWN_DYE);
        offerTintedGlassDyeingRecipe(exporter, GREEN_TINTED_GLASS_BLOCK, Items.GREEN_DYE);
        offerTintedGlassDyeingRecipe(exporter, RED_TINTED_GLASS_BLOCK, Items.RED_DYE);
        offerTintedGlassDyeingRecipe(exporter, BLACK_TINTED_GLASS_BLOCK, Items.BLACK_DYE);
    }

    private void offerTintedGlassDyeingRecipe(RecipeExporter exporter, ItemConvertible output, ItemConvertible input) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, output, 8)
                .input('#', Blocks.TINTED_GLASS)
                .input('X', input)
                .pattern("###")
                .pattern("#X#")
                .pattern("###")
                .group("tinted_glass")
                .criterion(hasItem(Items.TINTED_GLASS), conditionsFromItem(Items.TINTED_GLASS))
                .offerTo(exporter);
    }
}
