package me.monkeykiller.v2_0_rediscovered.common.superhostilemode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.monkeykiller.v2_0_rediscovered.common.configuration.ConfigUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SuperHostileModeInstance {
    private static final Set<SuperHostileModeInstance> REGISTRY = new HashSet<>();

    @Getter
    private static final Map<Class<? extends MobEntity>, EntityType<? extends MobEntity>> mobTypes = Map.of(
            SkeletonEntity.class, EntityType.SKELETON,
            ZombieEntity.class, EntityType.ZOMBIE,
            ZombifiedPiglinEntity.class, EntityType.ZOMBIFIED_PIGLIN,
            CreeperEntity.class, EntityType.CREEPER,
            BlazeEntity.class, EntityType.BLAZE
    );

    //

    @Getter
    private final ServerWorld world;

    private long nextSpawnWave = 0L, nextWaveReset = 0L;
    private final Set<MobEntity> waveEntities = new HashSet<>();

    public Set<MobEntity> getWaveEntities() {
        return Set.copyOf(waveEntities);
    }

    public boolean isSuperHostile() {
        return nextWaveReset > 0;
    }

    public void tick() {
        if (nextWaveReset > 0L) {
            if (--nextWaveReset > 0) return;
            for (var entity : waveEntities)
                entity.discard();
            waveEntities.clear();
            nextSpawnWave = world.getTime() + (MathHelper.nextBetween(world.random, ConfigUtils.getSuperHostileModeNextSpawnWaveCooldownMin(), ConfigUtils.getSuperHostileModeNextSpawnWaveCooldownMax()) * 20L);
        } else if (world.getTime() > nextSpawnWave) {
            nextWaveReset = MathHelper.nextBetween(world.random, ConfigUtils.getSuperHostileModeSpawnWaveDurationMin(), ConfigUtils.getSuperHostileModeSpawnWaveDurationMax()) * 20L;
            world.setWeather(0, (int) nextWaveReset, true, true);
            var players = world.getPlayers().stream().filter(p -> !ConfigUtils.isOnlyActivateOnSurvival() || p.interactionManager.getGameMode().isSurvivalLike()).toList();
            if (players.size() == 0) return;
            var waveSize = MathHelper.nextBetween(world.random, ConfigUtils.getSuperHostileModeSpawnWaveAttemptsMin(), ConfigUtils.getSuperHostileModeSpawnWaveAttemptsMax());
            var waveSizeSquared = waveSize * 2;
            var spawnedAmount = 0;
            var randomPlayer = players.get(world.random.nextInt(players.size()));
            if (ConfigUtils.isSuperHostileModeApplyBlindness())
                randomPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, (int) this.nextWaveReset, 0));

            for (int i = 0; i < waveSizeSquared && spawnedAmount < waveSize; i++) {
                MobEntity spawnedEntity;
                var rngX = world.random.nextGaussian() * 30.0D + randomPlayer.getX();
                var rngZ = world.random.nextGaussian() * 30.0D + randomPlayer.getZ();

                var entityIndex = world.random.nextInt(mobTypes.size());
                var clazz = mobTypes.keySet().stream().toList().get(entityIndex);
                var entityType = mobTypes.get(clazz);

                var lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
                lightning.setPos(rngX, randomPlayer.getY(), rngZ);

                if (ConfigUtils.isSuperHostileModeSpawnFakeLightning()) {
                    for (var player : world.getPlayers())
                        player.networkHandler.sendPacket(new EntitySpawnS2CPacket(lightning));
                } else world.spawnEntity(lightning);


                try {
                    spawnedEntity = clazz.getConstructor(EntityType.class, World.class).newInstance(entityType, world);
                    spawnedEntity.setPosition(rngX, randomPlayer.getY(), rngZ);
                    spawnedEntity.setTarget(randomPlayer);
                    spawnedEntity.initialize(world, world.getLocalDifficulty(spawnedEntity.getBlockPos()), SpawnReason.NATURAL, null, spawnedEntity.writeNbt(new NbtCompound()));
                    if (spawnedEntity instanceof CreeperEntity creeper && ConfigUtils.isSuperHostileModeSpawnFakeLightning())
                        creeper.onStruckByLightning(world, lightning);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }

                var collisions = StreamSupport.stream(world.getBlockCollisions(spawnedEntity, spawnedEntity.getBoundingBox()).spliterator(), false).toList();

                var aabbClear = world.getOtherEntities(null, spawnedEntity.getBoundingBox()).isEmpty();
                var noCollisions = collisions.isEmpty();
                var doesNotContainFluids = !world.containsFluid(spawnedEntity.getBoundingBox());
                if (aabbClear && noCollisions && doesNotContainFluids) {
                    spawnedAmount++;
                    spawnedEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 1000000, 127));
                    waveEntities.add(spawnedEntity);
                    world.spawnEntity(spawnedEntity);
                }
            }
        }
    }

    //

    public static SuperHostileModeInstance get(@NotNull ServerWorld world) {
        for (var inst : REGISTRY)
            if (inst.getWorld() == world)
                return inst;
        var inst = new SuperHostileModeInstance(world);
        REGISTRY.add(inst);
        return inst;
    }
}
