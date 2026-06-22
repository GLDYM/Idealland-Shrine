package cn.mcmod.ideallandshrine.api.kubejs;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public interface IdeallandShrineJSEvents {
    EventGroup EVENT_GROUP = EventGroup.of("IdeallandShrineEvents");

    EventHandler PRAY_SHRINE = EVENT_GROUP.server("prayShrine", () -> PrayShrineEventJS.class);
}
