package me.monkeykiller.v2_0_rediscovered.common.zombie_piglin_battlesign.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered.CONFIG_COMMON;

@Mixin(ZombifiedPiglinEntity.class)
public class ZombifiedPiglinEntityMixin {
    @Inject(at = @At("HEAD"), method = "initEquipment", cancellable = true)
    public void initEquipment(CallbackInfo ci) {
        if (!CONFIG_COMMON.pigman_battle_signs.enabled) return;
        var self = (ZombifiedPiglinEntity) (Object) this;
        ci.cancel();

        self.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.OAK_SIGN));
        var mainHand = self.getMainHandStack();

        if (!mainHand.isEmpty() && mainHand.isIn(ItemTags.SIGNS)) {
            var temporalSword = new ItemStack(Items.GOLDEN_SWORD);
            temporalSword.setNbt(mainHand.getOrCreateNbt());
            temporalSword = EnchantmentHelper.enchant(self.getRandom(), temporalSword, 30, false);

            mainHand.setNbt(temporalSword.getOrCreateNbt());
            self.equipStack(EquipmentSlot.MAINHAND, mainHand);
        }
    }
}
