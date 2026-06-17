package cn.mcmod.ideallandshrine.init.block;

import cn.mcmod.ideallandshrine.IdeallandShrine;
import cn.mcmod.ideallandshrine.block.FallProtectRunestoneBlock;
import cn.mcmod.ideallandshrine.block.GodRunestoneBlock;
import cn.mcmod.ideallandshrine.block.OutOfWorldRunestoneBlock;
import cn.mcmod.ideallandshrine.block.ShrineBaseBlock;
import cn.mcmod.ideallandshrine.data.GodShrineType;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class BlockRegistry {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(IdeallandShrine.MODID);

    public static final DeferredBlock<Block> SKYLAND_DULL_RUNESTONE = BLOCKS.register("skyland_dull_runestone",
            () -> new ShrineBaseBlock(baseProperties().lightLevel(state -> 0)));
    public static final DeferredBlock<Block> SKYLAND_OOW_RUNESTONE = BLOCKS.register("skyland_oow_runestone",
            () -> new OutOfWorldRunestoneBlock(baseProperties().lightLevel(state -> 15)));
    public static final DeferredBlock<Block> SKYLAND_FALL_RUNESTONE = BLOCKS.register("skyland_fall_runestone",
            () -> new FallProtectRunestoneBlock(baseProperties().lightLevel(state -> 15)));

    public static final List<DeferredBlock<? extends Block>> GOD_RUNESTONES = new ArrayList<>();

    static {
        for (GodShrineType type : GodShrineType.values()) {
            GOD_RUNESTONES.add(BLOCKS.register(type.blockName(),
                    () -> new GodRunestoneBlock(type, baseProperties().lightLevel(state -> 15))));
        }
    }

    private BlockRegistry() {
    }

    private static BlockBehaviour.Properties baseProperties() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .strength(1.5F, 6.0F)
                .sound(SoundType.STONE)
                .requiresCorrectToolForDrops();
    }
}
