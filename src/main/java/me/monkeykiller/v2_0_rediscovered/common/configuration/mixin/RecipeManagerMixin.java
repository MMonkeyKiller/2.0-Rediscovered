package me.monkeykiller.v2_0_rediscovered.common.configuration.mixin;

import com.google.gson.JsonElement;
import me.monkeykiller.v2_0_rediscovered.common.V2_0_Rediscovered;
import me.monkeykiller.v2_0_rediscovered.common.configuration.ConfigCommon;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Map;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin {
    @Inject(at = @At("HEAD"), method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V")
    private void apply(Map<Identifier, JsonElement> toAdd, ResourceManager resourceManager, Profiler profiler, CallbackInfo ci) {
        var toRemove = new HashSet<Identifier>();
        for (var entry : toAdd.entrySet()) {
            var id = entry.getKey();
            if (id.getNamespace().equals(V2_0_Rediscovered.MOD_ID)) {
                var enabled = true;
                if (id.getPath().contains("tinted_glass")) {
                    enabled = ConfigCommon.getConfig().tinted_glass.enabled;
                } else if (id.getPath().contains("etho_slab")) {
                    enabled = ConfigCommon.getConfig().etho_slab.enabled;
                } else if (id.getPath().contains("flopper")) {
                    enabled = ConfigCommon.getConfig().floppers.enabled;
                }
                if (!enabled) {
                    V2_0_Rediscovered.LOGGER.warn("Removing recipe {}", id);
                    toRemove.add(id);
                }
            }
        }

        for (var id : toRemove) {
            toAdd.remove(id);
        }
    }
}
