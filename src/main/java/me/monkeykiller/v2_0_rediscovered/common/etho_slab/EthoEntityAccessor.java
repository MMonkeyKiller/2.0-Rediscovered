package me.monkeykiller.v2_0_rediscovered.common.etho_slab;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.block.enums.SlabType;

public interface EthoEntityAccessor {
    EthoType getEthoType();

    void setEthoType(EthoType ethoType);

    @RequiredArgsConstructor
    enum EthoType {
        SINGLE_ETHO(SlabType.BOTTOM),
        DOUBLE_ETHO(SlabType.DOUBLE),
        NONE(null);

        @Getter
        private final SlabType slabType;
    }
}
