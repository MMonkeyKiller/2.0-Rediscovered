package me.monkeykiller.v2_0_rediscovered.common.floppers;

import com.mojang.logging.LogUtils;
import me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.slf4j.Logger;

import static me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered.CONFIG_COMMON;

public class FlopperBlock extends DispenserBlock {
    private static final DispenserBehavior FLOPPER_DISPENSER_BEHAVIOR = new FlopperItemDispenserBehavior();

    private static final Logger LOGGER = LogUtils.getLogger();

    public FlopperBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected DispenserBehavior getBehaviorForItem(World world, ItemStack stack) {
        return FLOPPER_DISPENSER_BEHAVIOR;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FlopperBlockEntity(pos, state);
    }

    @Override
    protected void dispense(ServerWorld world, BlockState state, BlockPos pos) {
        if (!CONFIG_COMMON.floppers.enabled) {
            super.dispense(world, state, pos);
            return;
        }

        ItemStack itemStack2;
        DispenserBlockEntity dispenserBlockEntity = world.getBlockEntity(pos, V2_0_Rediscovered.FLOPPER_BLOCK_ENTITY).orElse(null);
        if (dispenserBlockEntity == null) {
            LOGGER.warn("Ignoring dispensing attempt for Flopper without matching block entity at {}", pos);
            return;
        }
        BlockPointer blockPointer = new BlockPointer(world, pos, state, dispenserBlockEntity);
        int i = dispenserBlockEntity.chooseNonEmptySlot(world.random);
        if (i < 0) {
            world.syncWorldEvent(WorldEvents.DISPENSER_FAILS, pos, 0);
            return;
        }
        ItemStack itemStack = dispenserBlockEntity.getStack(i);
        if (itemStack.isEmpty()) {
            return;
        }
        Direction direction = world.getBlockState(pos).get(FACING);
        Inventory inventory = HopperBlockEntity.getInventoryAt(world, pos.offset(direction));
        if (inventory == null) {
            itemStack2 = FLOPPER_DISPENSER_BEHAVIOR.dispense(blockPointer, itemStack);
        } else {
            itemStack2 = HopperBlockEntity.transfer(dispenserBlockEntity, inventory, itemStack.copy().split(1), direction.getOpposite());
            if (itemStack2.isEmpty()) {
                itemStack2 = itemStack.copy();
                itemStack2.decrement(1);
            } else {
                itemStack2 = itemStack.copy();
            }
        }
        dispenserBlockEntity.setStack(i, itemStack2);
    }
}
