package cn.mcmod.ideallandshrine.block;

import cn.mcmod.ideallandshrine.data.ShrineDataAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class FallProtectRunestoneBlock extends ShrineBaseBlock {
    public FallProtectRunestoneBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
            BlockHitResult hitResult) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            ShrineDataAccess.addFallProtection(serverPlayer, 1);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
