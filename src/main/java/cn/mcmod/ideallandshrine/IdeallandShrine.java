package cn.mcmod.ideallandshrine;

import cn.mcmod.ideallandshrine.client.IdeallandShrineClient;
import cn.mcmod.ideallandshrine.config.ShrineConfig;
import cn.mcmod.ideallandshrine.init.CreativeTabRegistry;
import cn.mcmod.ideallandshrine.init.EntityTypeRegistry;
import cn.mcmod.ideallandshrine.init.block.BlockRegistry;
import cn.mcmod.ideallandshrine.init.item.BlockItemRegistry;
import cn.mcmod.ideallandshrine.init.item.ItemRegistry;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(IdeallandShrine.MODID)
public class IdeallandShrine {
    public static final String MODID = "idealland_shrine";

    public IdeallandShrine(IEventBus modEventBus, ModContainer modContainer) {
        BlockRegistry.BLOCKS.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);
        BlockItemRegistry.ITEMS.register(modEventBus);
        EntityTypeRegistry.ENTITY_TYPES.register(modEventBus);
        CreativeTabRegistry.TABS.register(modEventBus);
        modContainer.registerConfig(ModConfig.Type.SERVER, ShrineConfig.SPEC);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            IdeallandShrineClient.init(modEventBus);
        }
    }

    public static Item.Properties defaultItemProperties() {
        return new Item.Properties();
    }
}
