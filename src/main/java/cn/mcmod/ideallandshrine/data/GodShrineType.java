package cn.mcmod.ideallandshrine.data;

import cn.mcmod.ideallandshrine.config.ShrineConfig;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;

public enum GodShrineType {
    EARTH(entity -> {}),
    FIRE(entity -> entity.igniteForSeconds(1.0F)),
    LIFE(entity -> {
        entity.heal((float) ShrineConfig.lifeShrineHealAmount());
    }),
    SOIL(entity -> entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0, true, true))),
    WATER(entity -> {
        entity.clearFire();
        entity.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 100, 0, true, true));
    }),
    POISON(entity -> entity.getActiveEffects().stream()
            .filter(effect -> !effect.getEffect().value().isBeneficial())
            .map(MobEffectInstance::getEffect)
            .toList()
            .forEach(entity::removeEffect)),
    MALE(entity -> entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 0, true, true))),
    IRON(entity -> entity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 100, 0, true, true))),
    STONE(entity -> entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 0, true, true))),
    FEMALE(entity -> entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 0, true, true))),
    WOOD(entity -> {}),
    GOLD(entity -> entity.addEffect(new MobEffectInstance(MobEffects.LUCK, 100, 0, true, true))),
    WIND(entity -> {
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 0, true, true));
        entity.addEffect(new MobEffectInstance(MobEffects.JUMP, 100, 0, true, true));
    }),
    DEATH(entity -> {
        if (isDeathEffectTarget(entity)) {
            entity.hurt(entity.damageSources().magic(), (float) ShrineConfig.deathShrineDamage());
        }
    }),
    LAVA(entity -> entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 100, 0, true, true))),
    SKY(entity -> entity.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 220, 0, true, true)));

    private static final int COLUMN_HEIGHT = 8;
    private static final Field FURNACE_LIT_TIME_FIELD = findFurnaceField("litTime");
    private static final Field FURNACE_LIT_DURATION_FIELD = findFurnaceField("litDuration");

    private final Consumer<LivingEntity> touchEffect;

    GodShrineType(Consumer<LivingEntity> touchEffect) {
        this.touchEffect = touchEffect;
    }

    public String blockName() {
        return "skyland_runestone_" + ordinal();
    }

    public boolean canApplyEffectTo(LivingEntity entity) {
        return GodBeliefAccess.meetsEffectThreshold(entity, this);
    }

    public void applyTouchEffect(LivingEntity entity) {
        touchEffect.accept(entity);
    }

    public static boolean isDeathEffectTarget(LivingEntity entity) {
        return !(entity instanceof Player)
                && !ShrineConfig.isDeathShrineWhitelisted(entity)
                && (entity instanceof NeutralMob || entity instanceof Monster);
    }

    public void randomTick(Level level, BlockPos pos) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        // if (this == STONE) {
        //     placeTaggedColumn(serverLevel, pos, BlockTags.BASE_STONE_OVERWORLD);
        // }
        if (this == WOOD) {
            placeTaggedColumn(serverLevel, pos, BlockTags.LOGS);
        }
        if (this == STONE) {
            placeTaggedColumn(serverLevel, pos, Tags.Blocks.ORES);
        }
        if (this == EARTH) {
            growCropAt(serverLevel, pos.above(2));
        }
        if (this == FIRE) {
            igniteFurnaceBelow(serverLevel, pos.below());
        }
    }

    private static void placeTaggedColumn(ServerLevel level, BlockPos shrinePos, TagKey<Block> tagKey) {
        if (!isVerticalSpaceClear(level, shrinePos)) {
            return;
        }

        Optional<HolderSet.Named<Block>> optional = BuiltInRegistries.BLOCK.getTag(tagKey);
        if (optional.isEmpty()) {
            return;
        }

        HolderSet.Named<Block> holders = optional.get();
        if (holders.size() == 0) {
            return;
        }

        RandomSource random = level.getRandom();
        for (int i = 1; i <= COLUMN_HEIGHT; i++) {
            BlockPos targetPos = shrinePos.above(i);
            Block block = holders.get(random.nextInt(holders.size())).value();
            BlockState state = block.defaultBlockState();
            if (!state.canSurvive(level, targetPos)) {
                return;
            }
        }

        for (int i = 1; i <= COLUMN_HEIGHT; i++) {
            BlockPos targetPos = shrinePos.above(i);
            Block block = holders.get(random.nextInt(holders.size())).value();
            level.setBlockAndUpdate(targetPos, block.defaultBlockState());
        }
    }

    private static boolean isVerticalSpaceClear(ServerLevel level, BlockPos shrinePos) {
        for (int i = 1; i <= COLUMN_HEIGHT; i++) {
            if (!level.isEmptyBlock(shrinePos.above(i))) {
                return false;
            }
        }
        return true;
    }

    private static void growCropAt(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof CropBlock cropBlock && !cropBlock.isMaxAge(state)) {
            level.setBlockAndUpdate(pos, cropBlock.getStateForAge(cropBlock.getMaxAge()));
        }
    }

    private static void igniteFurnaceBelow(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof AbstractFurnaceBlockEntity furnace)) {
            return;
        }

        BlockState newState = state;
        setFurnaceBurnTime(furnace, ShrineConfig.fireShrineBurnTime());
        if (state.hasProperty(AbstractFurnaceBlock.LIT) && !state.getValue(AbstractFurnaceBlock.LIT)) {
            newState = state.setValue(AbstractFurnaceBlock.LIT, true);
            level.setBlock(pos, newState, Block.UPDATE_ALL);
        }
        furnace.setChanged();
        level.blockEntityChanged(pos);
        level.sendBlockUpdated(pos, state, newState, Block.UPDATE_ALL);
    }

    private static void setFurnaceBurnTime(AbstractFurnaceBlockEntity furnace, int burnTime) {
        trySetField(FURNACE_LIT_TIME_FIELD, furnace, burnTime);
        trySetField(FURNACE_LIT_DURATION_FIELD, furnace, burnTime);
    }

    private static void trySetField(Field field, AbstractFurnaceBlockEntity furnace, int value) {
        if (field == null) {
            return;
        }
        try {
            field.setInt(furnace, value);
        } catch (IllegalAccessException ignored) {
        }
    }

    private static Field findFurnaceField(String name) {
        try {
            Field field = AbstractFurnaceBlockEntity.class.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (ReflectiveOperationException ignored) {
            return null;
        }
    }
}
