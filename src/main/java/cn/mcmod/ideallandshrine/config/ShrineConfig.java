package cn.mcmod.ideallandshrine.config;

import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class ShrineConfig {
    public static final ModConfigSpec SPEC;

    private static final ModConfigSpec.IntValue FIRE_SHRINE_BURN_TIME;
    private static final ModConfigSpec.DoubleValue LIFE_SHRINE_HEAL_AMOUNT;
    private static final ModConfigSpec.DoubleValue LIFE_SHRINE_INVERTED_DAMAGE;
    private static final ModConfigSpec.DoubleValue DEATH_SHRINE_DAMAGE;
    private static final ModConfigSpec.ConfigValue<List<? extends String>> DEATH_SHRINE_ENTITY_WHITELIST;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.push("fire_shrine");
        FIRE_SHRINE_BURN_TIME = builder
                .comment("Burn time in ticks applied when the fire shrine ignites a furnace below it.")
                .defineInRange("burn_time", 1000, 0, Integer.MAX_VALUE);
        builder.pop();

        builder.push("life_shrine");
        LIFE_SHRINE_HEAL_AMOUNT = builder
                .comment("Healing dealt by the life shrine to normal living entities.")
                .defineInRange("heal_amount", 1.0D, 0.0D, 1024.0D);
        LIFE_SHRINE_INVERTED_DAMAGE = builder
                .comment("Damage dealt by the life shrine to inverted heal-and-harm entities.")
                .defineInRange("inverted_damage", 1.0D, 0.0D, 1024.0D);
        builder.pop();

        builder.push("death_shrine");
        DEATH_SHRINE_DAMAGE = builder
                .comment("Damage dealt by the death shrine to non-player entities that are not whitelisted.")
                .defineInRange("damage", 1.0D, 0.0D, 1024.0D);
        DEATH_SHRINE_ENTITY_WHITELIST = builder
                .comment("Entity ids ignored by the death shrine.")
                .defineListAllowEmpty("entity_whitelist", List.of("touhou_little_maid:maid"), () -> "touhou_little_maid:maid", ShrineConfig::isValidEntityId);
        builder.pop();
        SPEC = builder.build();
    }

    private ShrineConfig() {
    }

    public static int fireShrineBurnTime() {
        return FIRE_SHRINE_BURN_TIME.get();
    }

    public static double lifeShrineHealAmount() {
        return LIFE_SHRINE_HEAL_AMOUNT.get();
    }

    public static double lifeShrineInvertedDamage() {
        return LIFE_SHRINE_INVERTED_DAMAGE.get();
    }

    public static double deathShrineDamage() {
        return DEATH_SHRINE_DAMAGE.get();
    }

    public static boolean isDeathShrineWhitelisted(LivingEntity entity) {
        ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        if (id == null) {
            return false;
        }
        String entityId = id.toString();
        return DEATH_SHRINE_ENTITY_WHITELIST.get().stream().anyMatch(entityId::equals);
    }

    private static boolean isValidEntityId(Object value) {
        return value instanceof String string && ResourceLocation.tryParse(string) != null;
    }
}
