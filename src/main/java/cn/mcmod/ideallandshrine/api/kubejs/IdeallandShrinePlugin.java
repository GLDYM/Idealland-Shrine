package cn.mcmod.ideallandshrine.api.kubejs;

import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;

public class IdeallandShrinePlugin implements KubeJSPlugin {
    @Override
    public void registerEvents(EventGroupRegistry registry) {
        registry.register(IdeallandShrineJSEvents.EVENT_GROUP);
    }
}
