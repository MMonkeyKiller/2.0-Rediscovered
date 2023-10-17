package me.monkeykiller.v2_0_rediscovered.common.torch_off.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.TorchBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TorchBlock.class)
public class TorchBlockMixin {
    @ModifyVariable(method = "<init>", at = @At(value = "HEAD"), argsOnly = true)
    private static AbstractBlock.Settings init(AbstractBlock.Settings settings) {
        return settings.ticksRandomly();
    }
}
