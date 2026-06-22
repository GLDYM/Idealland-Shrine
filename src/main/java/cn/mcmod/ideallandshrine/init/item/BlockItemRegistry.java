package cn.mcmod.ideallandshrine.init.item;

import cn.mcmod.ideallandshrine.IdeallandShrine;
import cn.mcmod.ideallandshrine.data.GodShrineType;
import cn.mcmod.ideallandshrine.init.block.BlockRegistry;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class BlockItemRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(IdeallandShrine.MODID);

    public static final DeferredItem<Item> SKYLAND_DULL_RUNESTONE = ITEMS.register("skyland_dull_runestone",
            () -> new BlockItem(BlockRegistry.SKYLAND_DULL_RUNESTONE.get(), IdeallandShrine.defaultItemProperties()));
    public static final DeferredItem<Item> SKYLAND_OOW_RUNESTONE = ITEMS.register("skyland_oow_runestone",
            () -> new BlockItem(BlockRegistry.SKYLAND_OOW_RUNESTONE.get(), IdeallandShrine.defaultItemProperties()));
    public static final DeferredItem<Item> SKYLAND_FALL_RUNESTONE = ITEMS.register("skyland_fall_runestone",
            () -> new BlockItem(BlockRegistry.SKYLAND_FALL_RUNESTONE.get(), IdeallandShrine.defaultItemProperties()));
    public static final DeferredItem<Item> TELEPORTER_DIM_C16 = ITEMS.register("teleporter_dim_c16",
            () -> new BlockItem(BlockRegistry.TELEPORTER_DIM_C16.get(), IdeallandShrine.defaultItemProperties()));

    public static final List<DeferredItem<? extends Item>> GOD_RUNESTONES = new ArrayList<>();

    static {
        for (int index = 0; index < GodShrineType.values().length; index++) {
            GodShrineType type = GodShrineType.values()[index];
            final int currentIndex = index;
            GOD_RUNESTONES.add(ITEMS.register(type.blockName(),
                    () -> new BlockItem(BlockRegistry.GOD_RUNESTONES.get(currentIndex).get(),
                            IdeallandShrine.defaultItemProperties())));
        }
    }

    private BlockItemRegistry() {
    }
}
