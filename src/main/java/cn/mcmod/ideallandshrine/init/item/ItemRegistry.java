package cn.mcmod.ideallandshrine.init.item;

import cn.mcmod.ideallandshrine.IdeallandShrine;
import cn.mcmod.ideallandshrine.item.BasicRuneItem;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ItemRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(IdeallandShrine.MODID);
    public static final List<DeferredItem<Item>> BASIC_RUNES = new ArrayList<>();

    static {
        for (int index = 0; index < 16; index++) {
            final int currentIndex = index;
            String itemName = "basic16rune_" + currentIndex;
            BASIC_RUNES.add(ITEMS.register(itemName,
                    () -> new BasicRuneItem("item." + IdeallandShrine.MODID + "." + itemName + ".desc",
                            IdeallandShrine.defaultItemProperties())));
        }
    }

    private ItemRegistry() {
    }
}
