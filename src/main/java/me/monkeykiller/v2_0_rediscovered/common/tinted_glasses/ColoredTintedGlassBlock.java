package me.monkeykiller.v2_0_rediscovered.common.tinted_glasses;

import lombok.Getter;
import net.minecraft.block.TintedGlassBlock;

@Getter
public class ColoredTintedGlassBlock extends TintedGlassBlock {
    private final int color;

    public ColoredTintedGlassBlock(Settings settings, int color) {
        super(settings);
        this.color = color & 0xFFFFFF;
    }
}
