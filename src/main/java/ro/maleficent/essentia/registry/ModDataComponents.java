package ro.maleficent.essentia.registry;

import static ro.maleficent.essentia.Essentia.MOD_ID;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;

public class ModDataComponents {

    // Stored XP for Essential Vial (per stack)
    public static final DataComponentType<Integer> STORED_XP =
            Registry.register(
                    BuiltInRegistries.DATA_COMPONENT_TYPE,
                    ResourceLocation.fromNamespaceAndPath(MOD_ID, "stored_xp"),
                    DataComponentType.<Integer>builder()
                            .persistent(ExtraCodecs.NON_NEGATIVE_INT)
                            .build());

    public static void register(){
        // This method just ensures the static initializers run
        // Can add logs here
    }
}
