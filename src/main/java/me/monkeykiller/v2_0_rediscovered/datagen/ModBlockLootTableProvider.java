package me.monkeykiller.v2_0_rediscovered.datagen;

import me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered;
import me.monkeykiller.v2_0_rediscovered.common.etho_slab.EthoSlabBlock;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.condition.SurvivesExplosionLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.StatePredicate;

import static me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered.*;

public class ModBlockLootTableProvider extends FabricBlockLootTableProvider {
    public ModBlockLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        addDrop(ETHO_SLAB_BLOCK, createEthoSlabLootTable());
        addDrop(TORCH_OFF_BLOCK, createTorchOffLootTable());
    }

    private LootTable.Builder createEthoSlabLootTable() {
        return LootTable.builder().pool(
                LootPool.builder()
                        .bonusRolls(ConstantLootNumberProvider.create(0))
                        .conditionally(SurvivesExplosionLootCondition.builder())
                        .with(ItemEntry.builder(ETHO_SLAB_ITEM)
                                .conditionally(BlockStatePropertyLootCondition.builder(ETHO_SLAB_BLOCK)
                                        .properties(StatePredicate.Builder.create()
                                                .exactMatch(EthoSlabBlock.UNSTABLE, false))))
                        .rolls(ConstantLootNumberProvider.create(1))
                        .build()
        ).randomSequenceId(V2_0_Rediscovered.identifier("etho_slab"));
    }

    private LootTable.Builder createTorchOffLootTable() {
        return LootTable.builder().pool(
                LootPool.builder()
                        .bonusRolls(ConstantLootNumberProvider.create(0))
                        .conditionally(SurvivesExplosionLootCondition.builder())
                        .with(ItemEntry.builder(TORCH_OFF_ITEM))
                        .rolls(ConstantLootNumberProvider.create(1))
                        .build()
        );
    }
}
