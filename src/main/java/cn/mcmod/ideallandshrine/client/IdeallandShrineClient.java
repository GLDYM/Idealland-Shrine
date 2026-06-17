package cn.mcmod.ideallandshrine.client;

import cn.mcmod.ideallandshrine.client.renderer.CatharVexRenderer;
import cn.mcmod.ideallandshrine.init.EntityTypeRegistry;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber()
public final class IdeallandShrineClient {
    private IdeallandShrineClient() {
    }

    public static void init(IEventBus modEventBus) {
        modEventBus.addListener(IdeallandShrineClient::registerRenderers);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        EntityRenderers.register(EntityTypeRegistry.CATHAR_VEX.get(), CatharVexRenderer::new);
    }
}
