package cn.mcmod.ideallandshrine.block;

import cn.mcmod.ideallandshrine.data.GodShrineType;
import cn.mcmod.ideallandshrine.data.GodBeliefAccess;
import cn.mcmod.ideallandshrine.entity.CatharVexEntity;
import cn.mcmod.ideallandshrine.init.EntityTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class GodRunestoneBlock extends ShrineBaseBlock {
    private static final int BONUS_GUARDIAN_LIFETIME = 20 * 60 * 20;
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private final GodShrineType type;

    public GodRunestoneBlock(GodShrineType type, BlockBehaviour.Properties properties) {
        super(properties.randomTicks());
        this.type = type;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        super.stepOn(level, pos, state, entity);
        if (!level.isClientSide && entity instanceof LivingEntity livingEntity && type.canApplyEffectTo(livingEntity)) {
            type.applyTouchEffect(livingEntity);
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
            BlockHitResult hitResult) {
        if (!level.isClientSide) {
            Component shrineName = Component.translatable("block.idealland_shrine." + type.blockName());
            if (GodBeliefAccess.canPrayToday(player, type)) {
                int gainedBelief = 5 + level.getRandom().nextInt(11);
                GodBeliefAccess.addBelief(player, type, gainedBelief);
                GodBeliefAccess.markPrayedToday(player, type);
                int currentBelief = GodBeliefAccess.getBelief(player, type);
                player.displayClientMessage(
                        Component.translatable("message.idealland_shrine.pray_success", shrineName, currentBelief),
                        true);
                if (level instanceof ServerLevel serverLevel && currentBelief > 50) {
                    CatharVexEntity.summonGuardian(serverLevel, pos.above(), player, BONUS_GUARDIAN_LIFETIME);
                }
            } else {
                player.displayClientMessage(
                        Component.translatable("message.idealland_shrine.pray_already", shrineName,
                                GodBeliefAccess.getBelief(player, type)),
                        true);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        type.randomTick(level, pos);
        if (random.nextInt(4) == 0) {
            CatharVexEntity vex = new CatharVexEntity(EntityTypeRegistry.CATHAR_VEX.get(), level);
            vex.moveTo(Vec3.atBottomCenterOf(pos));
            vex.setBoundOrigin(pos);
            level.addFreshEntity(vex);
        }
    }

    public GodShrineType getType() {
        return type;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }
}
