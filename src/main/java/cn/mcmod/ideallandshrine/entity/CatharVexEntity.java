package cn.mcmod.ideallandshrine.entity;

import cn.mcmod.ideallandshrine.init.EntityTypeRegistry;
import java.util.EnumSet;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class CatharVexEntity extends Monster {
    private static final EntityDataAccessor<Boolean> CHARGING = SynchedEntityData.defineId(CatharVexEntity.class,
            EntityDataSerializers.BOOLEAN);
    private static final String BOUND_X = "BoundX";
    private static final String BOUND_Y = "BoundY";
    private static final String BOUND_Z = "BoundZ";
    private static final String LIFE_TICKS = "LifeTicks";
    private static final String OWNER_UUID = "OwnerUuid";

    @Nullable
    private BlockPos boundOrigin;
    @Nullable
    private UUID ownerUuid;
    private int limitedLifeTicks = 20 * 30;

    public CatharVexEntity(EntityType<? extends CatharVexEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new ShrineMoveControl(this);
        this.xpReward = 3;
        this.noPhysics = true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 32.0D)
                .add(Attributes.FLYING_SPEED, 0.7D)
                .add(Attributes.ATTACK_DAMAGE, 7.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.FOLLOW_RANGE, 16.0D);
    }

    public static CatharVexEntity summonGuardian(ServerLevel level, BlockPos pos, LivingEntity owner, int lifeTicks) {
        CatharVexEntity vex = new CatharVexEntity(EntityTypeRegistry.CATHAR_VEX.get(), level);
        vex.moveTo(Vec3.atBottomCenterOf(pos));
        vex.setBoundOrigin(pos);
        vex.setOwner(owner);
        vex.setLimitedLifeTicks(lifeTicks);
        level.addFreshEntity(vex);
        return vex;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(4, new ChargeAttackGoal());
        this.goalSelector.addGoal(8, new FollowOwnerGoal());
        this.goalSelector.addGoal(9, new WanderAroundGoal());
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(11, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new DefendOwnerGoal());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CHARGING, false);
    }

    @Override
    protected void populateDefaultEquipmentSlots(net.minecraft.util.RandomSource random,
            net.minecraft.world.DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
    }

    @Override
    public void tick() {
        this.noPhysics = true;
        super.tick();
        this.noPhysics = false;
        this.setNoGravity(true);
        if (!this.level().isClientSide && --limitedLifeTicks <= 0) {
            this.kill();
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypeTags.IS_FIRE)) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (boundOrigin != null) {
            compound.putInt(BOUND_X, boundOrigin.getX());
            compound.putInt(BOUND_Y, boundOrigin.getY());
            compound.putInt(BOUND_Z, boundOrigin.getZ());
        }
        compound.putInt(LIFE_TICKS, limitedLifeTicks);
        if (ownerUuid != null) {
            compound.putUUID(OWNER_UUID, ownerUuid);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains(BOUND_X, CompoundTag.TAG_INT)) {
            boundOrigin = new BlockPos(compound.getInt(BOUND_X), compound.getInt(BOUND_Y), compound.getInt(BOUND_Z));
        }
        if (compound.contains(LIFE_TICKS, CompoundTag.TAG_INT)) {
            limitedLifeTicks = compound.getInt(LIFE_TICKS);
        }
        if (compound.hasUUID(OWNER_UUID)) {
            ownerUuid = compound.getUUID(OWNER_UUID);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VEX_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VEX_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.VEX_HURT;
    }

    public boolean isCharging() {
        return this.entityData.get(CHARGING);
    }

    public void setCharging(boolean charging) {
        this.entityData.set(CHARGING, charging);
    }

    @Nullable
    public BlockPos getBoundOrigin() {
        return boundOrigin;
    }

    public void setBoundOrigin(@Nullable BlockPos boundOrigin) {
        this.boundOrigin = boundOrigin;
    }

    public void setLimitedLifeTicks(int limitedLifeTicks) {
        this.limitedLifeTicks = limitedLifeTicks;
    }

    public void setOwner(@Nullable LivingEntity owner) {
        this.ownerUuid = owner == null ? null : owner.getUUID();
    }

    @Nullable
    public LivingEntity getOwner() {
        if (!(level() instanceof ServerLevel serverLevel) || ownerUuid == null) {
            return null;
        }
        return serverLevel.getEntity(ownerUuid) instanceof LivingEntity livingEntity ? livingEntity : null;
    }

    class ChargeAttackGoal extends Goal {
        ChargeAttackGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return getTarget() != null && !getMoveControl().hasWanted() && random.nextInt(7) == 0
                    && distanceToSqr(getTarget()) > 4.0D;
        }

        @Override
        public boolean canContinueToUse() {
            return getMoveControl().hasWanted() && isCharging() && getTarget() != null && getTarget().isAlive();
        }

        @Override
        public void start() {
            LivingEntity target = getTarget();
            if (target != null) {
                Vec3 vec3 = target.getEyePosition();
                getMoveControl().setWantedPosition(vec3.x, vec3.y, vec3.z, 1.0D);
                setCharging(true);
                playSound(SoundEvents.VEX_CHARGE, 1.0F, 1.0F);
            }
        }

        @Override
        public void stop() {
            setCharging(false);
        }

        @Override
        public void tick() {
            LivingEntity target = getTarget();
            if (target == null) {
                return;
            }
            if (getBoundingBox().intersects(target.getBoundingBox())) {
                doHurtTarget(target);
                setCharging(false);
            } else if (distanceToSqr(target) < 9.0D) {
                Vec3 vec3 = target.getEyePosition();
                getMoveControl().setWantedPosition(vec3.x, vec3.y, vec3.z, 1.0D);
            }
        }
    }

    class FollowOwnerGoal extends Goal {
        FollowOwnerGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            LivingEntity owner = getOwner();
            return owner != null && getTarget() == null && distanceToSqr(owner) > 9.0D;
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity owner = getOwner();
            return owner != null && getTarget() == null && distanceToSqr(owner) > 4.0D;
        }

        @Override
        public void tick() {
            LivingEntity owner = getOwner();
            if (owner == null) {
                return;
            }
            Vec3 vec3 = owner.getEyePosition();
            getMoveControl().setWantedPosition(vec3.x, vec3.y, vec3.z, 0.5D);
        }
    }

    class DefendOwnerGoal extends Goal {
        DefendOwnerGoal() {
            this.setFlags(EnumSet.of(Flag.TARGET));
        }

        @Override
        public boolean canUse() {
            LivingEntity owner = getOwner();
            if (owner == null) {
                return false;
            }

            LivingEntity attacker = owner.getLastHurtByMob();
            if (attacker == null || !attacker.isAlive()) {
                attacker = owner.getLastHurtMob();
            }
            if (attacker == null || !attacker.isAlive() || attacker == CatharVexEntity.this) {
                return false;
            }

            setTarget(attacker);
            return true;
        }
    }

    class WanderAroundGoal extends Goal {
        WanderAroundGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return getOwner() == null && !getMoveControl().hasWanted() && random.nextInt(7) == 0;
        }

        @Override
        public void tick() {
            BlockPos center = getBoundOrigin() == null ? blockPosition() : getBoundOrigin();
            for (int i = 0; i < 3; ++i) {
                BlockPos next = center.offset(random.nextInt(15) - 7, random.nextInt(11) - 5, random.nextInt(15) - 7);
                if (level().isEmptyBlock(next)) {
                    getMoveControl().setWantedPosition(next.getX() + 0.5D, next.getY() + 0.5D, next.getZ() + 0.5D,
                            0.25D);
                    break;
                }
            }
        }
    }

    static class ShrineMoveControl extends MoveControl {
        private final CatharVexEntity vex;

        ShrineMoveControl(CatharVexEntity vex) {
            super(vex);
            this.vex = vex;
        }

        @Override
        public void tick() {
            if (this.operation == Operation.MOVE_TO) {
                Vec3 delta = new Vec3(this.wantedX - vex.getX(), this.wantedY - vex.getY(), this.wantedZ - vex.getZ());
                double length = delta.length();
                if (length < vex.getBoundingBox().getSize()) {
                    this.operation = Operation.WAIT;
                    vex.setDeltaMovement(vex.getDeltaMovement().scale(0.5D));
                    return;
                }

                Vec3 scaled = delta.scale(this.speedModifier * 0.05D / length);
                vex.setDeltaMovement(vex.getDeltaMovement().add(scaled));
                if (vex.getTarget() == null) {
                    Vec3 move = vex.getDeltaMovement();
                    vex.setYRot(-((float) Math.toDegrees(Math.atan2(move.x, move.z))));
                } else {
                    double dx = vex.getTarget().getX() - vex.getX();
                    double dz = vex.getTarget().getZ() - vex.getZ();
                    vex.setYRot(-((float) Math.toDegrees(Math.atan2(dx, dz))));
                }
                vex.yBodyRot = vex.getYRot();
            }
        }
    }
}
