package ro.maleficent.essentia.registry;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import ro.maleficent.essentia.item.EssentiaVialItem;

import static ro.maleficent.essentia.Essentia.MOD_ID;

public class ModItems {
    public static final ResourceKey<Item> ESSENTIAL_VIAL_KEY = ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID,"essentia_vial"));

    public static final Item ESSENTIA_VIAL = Items.registerItem(ESSENTIAL_VIAL_KEY,EssentiaVialItem::new,
            new Item.Properties()
                    .stacksTo(1)
                    .durability(EssentiaVialItem.CAPACITY));

    public static void register(){
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(entries ->{
            entries.accept(ESSENTIA_VIAL);
        });
    }
}
