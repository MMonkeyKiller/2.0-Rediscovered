package me.monkeykiller.v2_0_rediscovered.common.floppers;

import me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class FlopperBlockEntity extends DispenserBlockEntity {
    public FlopperBlockEntity(BlockPos pos, BlockState state) {
        super(V2_0_Rediscovered.FLOPPER_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("container.flopper");
    }
}
