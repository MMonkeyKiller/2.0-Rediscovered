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
        translationBuilder.add(ETHO_SLAB_BLOCK, "Etho Slab");
        translationBuilder.add(TORCH_OFF_BLOCK, "Torch (Burnt-out)");

        translationBuilder.add(WITHER_HUG, "Pink Wither");
        translationBuilder.add(WITHER_LOVE, "Pink Wither Love");
    }
}
