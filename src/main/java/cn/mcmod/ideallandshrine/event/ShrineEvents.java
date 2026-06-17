package cn.mcmod.ideallandshrine.event;

import cn.mcmod.ideallandshrine.IdeallandShrine;
import cn.mcmod.ideallandshrine.data.ShrineDataAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber(modid = IdeallandShrine.MODID)
public final class ShrineEvents {
    private ShrineEvents() {
    }

    @SubscribeEvent
    public static void onIncomingDamage(LivingIncomingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof ServerPlayer serverPlayer)) {
            return;
        }

        if (event.getSource().is(DamageTypes.FALL) && ShrineDataAccess.getFallProtection(serverPlayer) > 0) {
            ShrineDataAccess.addFallProtection(serverPlayer, -1);
            serverPlayer.displayClientMessage(Component.translatable("message.idealland_shrine.fall_protect_used",
                    ShrineDataAccess.getFallProtection(serverPlayer)), true);
            event.setCanceled(true);
            return;
        }

        if (event.getSource().is(DamageTypes.FELL_OUT_OF_WORLD) && ShrineDataAccess.hasOutOfWorldProtection(serverPlayer)
                && serverPlayer.getY() < serverPlayer.level().getMinBuildHeight()) {
            ShrineDataAccess.setOutOfWorldProtection(serverPlayer, false);
            ShrineDataAccess.addFallProtection(serverPlayer, 1);
            serverPlayer.teleportTo(serverPlayer.serverLevel(), serverPlayer.getX(), 260.0D, serverPlayer.getZ(),
                    serverPlayer.getYRot(), serverPlayer.getXRot());
            serverPlayer.displayClientMessage(Component.translatable("message.idealland_shrine.oow_protect_used"),
                    true);
            event.setCanceled(true);
        }
    }
}
