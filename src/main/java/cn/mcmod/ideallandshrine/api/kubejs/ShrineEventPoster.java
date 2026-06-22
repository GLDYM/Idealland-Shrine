package cn.mcmod.ideallandshrine.api.kubejs;

import cn.mcmod.ideallandshrine.api.event.PrayShrineEvent;
import net.neoforged.fml.ModList;

public class ShrineEventPoster {
    public static final ShrineEventPoster INSTANCE = new ShrineEventPoster();

    private ShrineEventPoster() {
    }

    public void post(PrayShrineEvent event) {
        if (ModList.get().isLoaded("kubejs")) {
            post(new PrayShrineEventJS(event));
        }
    }

    public void post(PrayShrineEventJS event) {
        IdeallandShrineJSEvents.PRAY_SHRINE.post(event);
    }
}
