package cn.mcmod.ideallandshrine.init;

import cn.mcmod.ideallandshrine.IdeallandShrine;
import cn.mcmod.ideallandshrine.init.item.BlockItemRegistry;
import cn.mcmod.ideallandshrine.init.item.ItemRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class CreativeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB,
            IdeallandShrine.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN = TABS.register("main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + IdeallandShrine.MODID))
                    .icon(() -> BlockItemRegistry.SKYLAND_DULL_RUNESTONE.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(BlockItemRegistry.SKYLAND_DULL_RUNESTONE.get());
                        output.accept(BlockItemRegistry.SKYLAND_OOW_RUNESTONE.get());
                        output.accept(BlockItemRegistry.SKYLAND_FALL_RUNESTONE.get());
                        output.accept(BlockItemRegistry.TELEPORTER_DIM_C16.get());
                        BlockItemRegistry.GOD_RUNESTONES.forEach(holder -> output.accept(holder.get()));
                        ItemRegistry.BASIC_RUNES.forEach(holder -> output.accept(holder.get()));
                    })
                    .build());

    private CreativeTabRegistry() {
    }
}
