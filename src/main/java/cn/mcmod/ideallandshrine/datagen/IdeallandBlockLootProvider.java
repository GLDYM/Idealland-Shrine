package cn.mcmod.ideallandshrine.datagen;

import cn.mcmod.ideallandshrine.init.block.BlockRegistry;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.data.loot.BlockLootSubProvider;

public class IdeallandBlockLootProvider extends BlockLootSubProvider {
    protected IdeallandBlockLootProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(BlockRegistry.SKYLAND_DULL_RUNESTONE.get());
        dropSelf(BlockRegistry.SKYLAND_OOW_RUNESTONE.get());
        dropSelf(BlockRegistry.SKYLAND_FALL_RUNESTONE.get());
        dropSelf(BlockRegistry.TELEPORTER_DIM_C16.get());
        BlockRegistry.GOD_RUNESTONES.forEach(deferredBlock -> dropSelf(deferredBlock.get()));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BlockRegistry.BLOCKS.getEntries().stream()
                .map(entry -> (Block) entry.get())
                .collect(Collectors.toList());
    }
}
