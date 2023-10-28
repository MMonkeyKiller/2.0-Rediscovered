package me.monkeykiller.v2_0_rediscovered.common.floppers;

import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPointer;

public class FlopperItemDispenserBehavior extends ItemDispenserBehavior {
    @Override
    protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
        var fishStack = new ItemStack(Items.COD, stack.getCount());
        super.dispenseSilently(pointer, fishStack);
        stack.decrement(1);
        return stack;
    }
}
