package cn.mcmod.ideallandshrine.datagen;

import cn.mcmod.ideallandshrine.IdeallandShrine;
import cn.mcmod.ideallandshrine.init.item.ItemRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

public class IdeallandItemModelProvider extends ItemModelProvider {
    public IdeallandItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, IdeallandShrine.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ItemRegistry.BASIC_RUNES.forEach(this::registerBasicRuneItem);
    }

    private void registerBasicRuneItem(DeferredItem<Item> item) {
        String itemName = item.getId().getPath();
        withExistingParent(itemName, mcLoc("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(IdeallandShrine.MODID, "item/basic/" + itemName));
    }
}
