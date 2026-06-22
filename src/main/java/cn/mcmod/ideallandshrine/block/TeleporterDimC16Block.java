package cn.mcmod.ideallandshrine.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TeleporterDimC16Block extends ShrineBaseBlock {
    private static final VoxelShape SHAPE = Shapes.or(
            box(0.0D, 8.0D, 0.0D, 16.0D, 9.0D, 1.0D),
            box(0.0D, 8.0D, 15.0D, 16.0D, 9.0D, 16.0D),
            box(0.0D, 8.0D, 1.0D, 1.0D, 9.0D, 15.0D),
            box(15.0D, 8.0D, 1.0D, 16.0D, 9.0D, 15.0D),
            box(2.0D, 13.0D, 7.5D, 14.0D, 14.0D, 8.5D),
            box(2.0D, 2.0D, 7.5D, 14.0D, 3.0D, 8.5D),
            box(2.0D, 3.0D, 7.5D, 3.0D, 13.0D, 8.5D),
            box(13.0D, 3.0D, 7.5D, 14.0D, 13.0D, 8.5D),
            box(7.5D, 11.0D, 4.0D, 8.5D, 12.0D, 12.0D),
            box(7.5D, 5.0D, 11.0D, 8.5D, 11.0D, 12.0D),
            box(7.5D, 5.0D, 4.0D, 8.5D, 11.0D, 5.0D),
            box(7.5D, 4.0D, 4.0D, 8.5D, 5.0D, 12.0D),
            box(6.0D, 6.0D, 6.0D, 10.0D, 10.0D, 10.0D));

    public TeleporterDimC16Block(BlockBehaviour.Properties properties) {
        super(properties);
    }

    // @Override
    // protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    //     return SHAPE;
    // }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}
