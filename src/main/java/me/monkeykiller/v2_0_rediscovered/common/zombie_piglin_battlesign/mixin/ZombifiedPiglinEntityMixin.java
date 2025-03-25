package me.monkeykiller.v2_0_rediscovered.common.zombie_piglin_battlesign.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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

        var temporalSword = EnchantmentHelper.enchant(self.getWorld().getEnabledFeatures(), self.getRandom(), new ItemStack(Items.GOLDEN_SWORD), 30, false);
        self.equipStack(EquipmentSlot.MAINHAND, temporalSword.copyComponentsToNewStack(Items.OAK_SIGN, 1));
    }
}
