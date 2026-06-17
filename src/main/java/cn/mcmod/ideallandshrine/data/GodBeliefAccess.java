package cn.mcmod.ideallandshrine.data;

import cn.mcmod.ideallandshrine.IdeallandShrine;
import cn.mcmod.ideallandshrine.entity.CatharVexEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public final class GodBeliefAccess {
    private static final String ROOT = IdeallandShrine.MODID;
    private static final String LEGACY_BELIEF_PREFIX = "god16tri_";
    private static final String PRAYER_ROOT = "PrayerCooldowns";
    private static final String LAST_PRAY_DAY_PREFIX = "idealland_shrine_last_pray_";
    private static final int CATHAR_VEX_BELIEF_VALUE = 100;
    private static final int DEFAULT_SPECIAL_BELIEF = 100;
    private static final int DEFAULT_SPECIAL_DISFAVOR = -100;

    private GodBeliefAccess() {
    }

    public static int getBelief(LivingEntity entity, GodShrineType type) {
        if (entity instanceof CatharVexEntity) {
            return CATHAR_VEX_BELIEF_VALUE;
        }

        CompoundTag tag = entity.getPersistentData();
        String key = beliefKey(type);
        if (tag.contains(key, CompoundTag.TAG_INT)) {
            return tag.getInt(key);
        }
        return defaultBelief(entity, type);
    }

    public static void addBelief(LivingEntity entity, GodShrineType type, int amount) {
        setBelief(entity, type, getBelief(entity, type) + amount);
    }

    public static void setBelief(LivingEntity entity, GodShrineType type, int value) {
        entity.getPersistentData().putInt(beliefKey(type), value);
    }

    public static boolean canPrayToday(Player player, GodShrineType type) {
        long currentDay = player.level().getDayTime() / 24000L;
        return prayerRoot(player).getLong(lastPrayDayKey(type)) != currentDay;
    }

    public static void markPrayedToday(Player player, GodShrineType type) {
        long currentDay = player.level().getDayTime() / 24000L;
        prayerRoot(player).putLong(lastPrayDayKey(type), currentDay);
    }

    public static boolean meetsEffectThreshold(LivingEntity entity, GodShrineType type) {
        if (type == GodShrineType.FIRE) {
            return true;
        }
        if (type == GodShrineType.DEATH) {
            return GodShrineType.isDeathEffectTarget(entity);
        }
        int belief = getBelief(entity, type);
        return belief > 50;
    }

    private static int defaultBelief(LivingEntity entity, GodShrineType type) {
        if (entity.isInvertedHealAndHarm()) {
            if (type == GodShrineType.DEATH) {
                return DEFAULT_SPECIAL_BELIEF;
            }
            if (type == GodShrineType.LIFE) {
                return DEFAULT_SPECIAL_DISFAVOR;
            }
        }
        return 0;
    }

    private static String beliefKey(GodShrineType type) {
        return LEGACY_BELIEF_PREFIX + type.ordinal();
    }

    private static String lastPrayDayKey(GodShrineType type) {
        return LAST_PRAY_DAY_PREFIX + type.ordinal();
    }

    private static CompoundTag prayerRoot(Player player) {
        CompoundTag persistentData = player.getPersistentData();
        if (!persistentData.contains(ROOT, CompoundTag.TAG_COMPOUND)) {
            persistentData.put(ROOT, new CompoundTag());
        }
        CompoundTag root = persistentData.getCompound(ROOT);
        if (!root.contains(PRAYER_ROOT, CompoundTag.TAG_COMPOUND)) {
            root.put(PRAYER_ROOT, new CompoundTag());
        }
        return root.getCompound(PRAYER_ROOT);
    }
}
