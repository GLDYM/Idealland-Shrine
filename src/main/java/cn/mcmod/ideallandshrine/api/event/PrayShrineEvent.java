package cn.mcmod.ideallandshrine.api.event;

import cn.mcmod.ideallandshrine.data.GodShrineType;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;

public class PrayShrineEvent extends Event {
    private final ServerPlayer player;
    private final GodShrineType god;
    private final int beforeBelief;
    private final int afterBelief;

    public PrayShrineEvent(ServerPlayer player, GodShrineType god, int beforeBelief, int afterBelief) {
        this.player = player;
        this.god = god;
        this.beforeBelief = beforeBelief;
        this.afterBelief = afterBelief;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    public GodShrineType getGod() {
        return god;
    }

    public int getBeforeBelief() {
        return beforeBelief;
    }

    public int getAfterBelief() {
        return afterBelief;
    }
}
