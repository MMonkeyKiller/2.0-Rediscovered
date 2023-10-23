package me.monkeykiller.v2_0_rediscovered.common.pink_wither;

import me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

public class WitherLoveEntity extends ExplosiveProjectileEntity {
    public WitherLoveEntity(EntityType<? extends ExplosiveProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public WitherLoveEntity(World world, WitherHugEntity witherHugEntity, double g, double h, double i) {
        super(V2_0_Rediscovered.WITHER_LOVE, witherHugEntity, g, h, i, world);
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        var pos = this.getBlockPos();
        if (!this.getWorld().isClient()) {
            for (int x = -7; x <= 7; ++x) {
                for (int z = -7; z <= 7; ++z) {
                    for (int y = -1; y <= 3; ++y) {
                        var blockPos = pos.add(x, y, z);

                        if (BoneMealItem.useOnFertilizable(new ItemStack(Items.AIR), this.getWorld(), blockPos) && !this.getWorld().isClient()) {
                            this.getWorld().syncWorldEvent(WorldEvents.BONE_MEAL_USED, blockPos, 0);
                        }
                    }
                }
            }
            this.discard();
        }
    }

    @Override
    public boolean collidesWith(Entity other) {
        return false;
    }
}
