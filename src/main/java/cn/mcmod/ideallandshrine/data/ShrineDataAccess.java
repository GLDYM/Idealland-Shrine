package cn.mcmod.ideallandshrine.data;

import cn.mcmod.ideallandshrine.IdeallandShrine;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public final class ShrineDataAccess {
    private static final String ROOT = IdeallandShrine.MODID;
    private static final String FALL_PROTECT = "FallProtect";
    private static final String OUT_OF_WORLD_PROTECT = "OutOfWorldProtect";

    private ShrineDataAccess() {
    }

    private static CompoundTag root(Player player) {
        CompoundTag persistentData = player.getPersistentData();
        if (!persistentData.contains(ROOT, CompoundTag.TAG_COMPOUND)) {
            persistentData.put(ROOT, new CompoundTag());
        }
        return persistentData.getCompound(ROOT);
    }

    public static int getFallProtection(Player player) {
        return root(player).getInt(FALL_PROTECT);
    }

    public static void addFallProtection(Player player, int amount) {
        CompoundTag tag = root(player);
        tag.putInt(FALL_PROTECT, Math.max(0, tag.getInt(FALL_PROTECT) + amount));
    }

    public static boolean hasOutOfWorldProtection(Player player) {
        return root(player).getBoolean(OUT_OF_WORLD_PROTECT);
    }

    public static void setOutOfWorldProtection(Player player, boolean value) {
        root(player).putBoolean(OUT_OF_WORLD_PROTECT, value);
    }
}
