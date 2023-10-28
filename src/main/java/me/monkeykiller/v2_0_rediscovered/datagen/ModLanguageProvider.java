package me.monkeykiller.v2_0_rediscovered.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import static me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered.*;

public class ModLanguageProvider extends FabricLanguageProvider {
    protected ModLanguageProvider(FabricDataOutput dataOutput) {
        super(dataOutput, "en_us");
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add("container.flopper", "Flopper");

        translationBuilder.add(ETHO_SLAB_BLOCK, "Etho Slab");
        translationBuilder.add(TORCH_OFF_BLOCK, "Torch (Burnt-out)");
        translationBuilder.add(FLOPPER_BLOCK, "Flopper");

        translationBuilder.add(BLACK_TINTED_GLASS_BLOCK, "Black Tinted Glass");
        translationBuilder.add(RED_TINTED_GLASS_BLOCK, "Red Tinted Glass");
        translationBuilder.add(GREEN_TINTED_GLASS_BLOCK, "Green Tinted Glass");
        translationBuilder.add(BROWN_TINTED_GLASS_BLOCK, "Brown Tinted Glass");
        translationBuilder.add(BLUE_TINTED_GLASS_BLOCK, "Blue Tinted Glass");
        translationBuilder.add(PURPLE_TINTED_GLASS_BLOCK, "Purple Tinted Glass");
        translationBuilder.add(CYAN_TINTED_GLASS_BLOCK, "Cyan Tinted Glass");
        translationBuilder.add(SILVER_TINTED_GLASS_BLOCK, "Light Gray Tinted Glass");
        translationBuilder.add(GRAY_TINTED_GLASS_BLOCK, "Gray Tinted Glass");
        translationBuilder.add(PINK_TINTED_GLASS_BLOCK, "Pink Tinted Glass");
        translationBuilder.add(LIME_TINTED_GLASS_BLOCK, "Lime Tinted Glass");
        translationBuilder.add(YELLOW_TINTED_GLASS_BLOCK, "Yellow Tinted Glass");
        translationBuilder.add(LIGHT_BLUE_TINTED_GLASS_BLOCK, "Light Blue Tinted Glass");
        translationBuilder.add(MAGENTA_TINTED_GLASS_BLOCK, "Magenta Tinted Glass");
        translationBuilder.add(ORANGE_TINTED_GLASS_BLOCK, "Orange Tinted Glass");
        translationBuilder.add(WHITE_TINTED_GLASS_BLOCK, "White Tinted Glass");

        translationBuilder.add(WITHER_HUG, "Pink Wither");
        translationBuilder.add(WITHER_LOVE, "Pink Wither Love");
    }
}
