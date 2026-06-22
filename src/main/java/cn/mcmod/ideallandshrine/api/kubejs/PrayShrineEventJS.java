package cn.mcmod.ideallandshrine.api.kubejs;

import cn.mcmod.ideallandshrine.api.event.PrayShrineEvent;
import cn.mcmod.ideallandshrine.data.GodShrineType;
import dev.latvian.mods.kubejs.event.KubeEvent;
import net.minecraft.server.level.ServerPlayer;

public class PrayShrineEventJS implements KubeEvent {
    private final PrayShrineEvent event;

    public PrayShrineEventJS(PrayShrineEvent event) {
        this.event = event;
    }

    public ServerPlayer getPlayer() {
        return event.getPlayer();
    }

    public GodShrineType getGod() {
        return event.getGod();
    }

    public String getGodId() {
        return event.getGod().name().toLowerCase();
    }

    public int getBeforeBelief() {
        return event.getBeforeBelief();
    }

    public int getAfterBelief() {
        return event.getAfterBelief();
    }
}
