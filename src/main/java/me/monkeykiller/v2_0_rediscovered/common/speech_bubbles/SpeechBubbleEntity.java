package me.monkeykiller.v2_0_rediscovered.common.speech_bubbles;

import me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class SpeechBubbleEntity extends Entity {
    private static final TrackedData<String> BUBBLE_TEXT = DataTracker.registerData(SpeechBubbleEntity.class, TrackedDataHandlerRegistry.STRING);

    private static final String[] texts;
    private static final Random random = new Random();
    private int remainingTicks = 20;

    public SpeechBubbleEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public SpeechBubbleEntity(World world, int x, int y, int z) {
        this(world, x + 0.5, y + 0.5, z + 0.5);
    }

    public SpeechBubbleEntity(World world, double x, double y, double z) {
        super(V2_0_Rediscovered.SPEECH_BUBBLE, world);
        this.setPosition(x, y, z);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(BUBBLE_TEXT, "");
    }

    public void setBubbleText(@NotNull String value) {
        this.dataTracker.set(BUBBLE_TEXT, value);
    }

    public String getBubbleText() {
        return this.dataTracker.get(BUBBLE_TEXT);
    }

    @Override
    public void tick() {
        this.prevX = this.getX();
        this.prevY = this.getY();
        this.prevZ = this.getZ();

        if (remainingTicks-- < 0) {
            this.discard();
        }

        this.setVelocity(0, 0, 0);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
    }

    public static String getRandomText() {
        return texts[random.nextInt(texts.length)];
    }

    static {
        texts = new String[]{
                "That hurts :(",
                "My eye!",
                "..zzZZZ",
                "Why!?",
                "I'll be back!",
                "You didn't ....",
                "Oof",
                "(&%!@*#&%!@$#",
                "Ouch",
                "Hi there!",
                "That tickles",
                "Who's there!",
                "I thought we were friends",
                "You're mean!",
                "Whiiii",
                "Not dropping anything!",
                "I like you!",
                "I HATE you!",
                "YOU NO TAKE .. COBBLE!",
                "I'm diamond",
                "You punched me",
                "Stop punching me",
                "I'm dirt",
                "Mummy?",
                "SSSSSSST",
                "KABOOM?",
                "BULLY!",
                "I'm telling",
                "Creeper behind you!",
                "Did I do bad?",
                "Hello"
        };
    }
}
