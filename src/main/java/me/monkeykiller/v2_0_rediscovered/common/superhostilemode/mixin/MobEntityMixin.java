package me.monkeykiller.v2_0_rediscovered.common.superhostilemode.mixin;

import me.monkeykiller.v2_0_rediscovered.common.superhostilemode.SuperHostileModeInstance;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(MobEntity.class)
public class MobEntityMixin {
    @Inject(at = @At(value = "HEAD"), method = "initEquipment", cancellable = true)
    public void superHostileModeInitEquipment(Random random, LocalDifficulty localDifficulty, CallbackInfo ci) {
        final var self = ((MobEntity) ((Object) this));
        final var entityType = self.getType();

        if (!(self.getWorld() instanceof ServerWorld serverWorld)) return;
        if (SuperHostileModeInstance.getMobTypes().values().stream().noneMatch(entityType::equals)) return;
        if (!SuperHostileModeInstance.get(serverWorld).isSuperHostile()) return;
        if (!(entityType == EntityType.SKELETON || entityType == EntityType.ZOMBIE || entityType == EntityType.ZOMBIFIED_PIGLIN))
            return;
        ci.cancel();

        int armorLevel = MathHelper.nextBetween(self.getRandom(), 2, 4);
        for (var slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) continue;

            var stack = new ItemStack(MobEntity.getEquipmentForSlot(slot, armorLevel));
            if (slot == EquipmentSlot.HEAD && self.getRandom().nextFloat() < 0.25D) {
                var wools = Registries.BLOCK.stream().filter(b -> b.getRegistryEntry().isIn(BlockTags.WOOL)).toList();
                stack = new ItemStack(wools.get(self.getRandom().nextInt(wools.size())));
            }
            EnchantmentHelper.enchant(self.getRandom(), stack, 30, serverWorld.getRegistryManager(), Optional.empty());
            self.equipStack(slot, stack);
        }

        ItemStack stack;
        if (entityType == EntityType.SKELETON) {
            stack = new ItemStack(Items.BOW);
        } else if (entityType == EntityType.ZOMBIFIED_PIGLIN) {
            stack = new ItemStack(Items.GOLDEN_SWORD);
        } else {
            stack = new ItemStack(Items.DIAMOND_SWORD);
        }
        EnchantmentHelper.enchant(self.getRandom(), stack, 30, serverWorld.getRegistryManager(), Optional.empty());
        self.equipStack(EquipmentSlot.MAINHAND, stack);
    }
}
