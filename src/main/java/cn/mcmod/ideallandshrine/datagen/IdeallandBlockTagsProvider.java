package cn.mcmod.ideallandshrine.datagen;

import cn.mcmod.ideallandshrine.IdeallandShrine;
import cn.mcmod.ideallandshrine.init.block.BlockRegistry;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class IdeallandBlockTagsProvider extends BlockTagsProvider {
    public IdeallandBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
            ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, IdeallandShrine.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        addAllShrineBlocks(tag(BlockTags.MINEABLE_WITH_PICKAXE));
        addAllShrineBlocks(tag(BlockTags.NEEDS_STONE_TOOL));
    }

    private void addAllShrineBlocks(IntrinsicHolderTagsProvider.IntrinsicTagAppender<Block> tag) {
        tag.add(BlockRegistry.SKYLAND_DULL_RUNESTONE.get());
        tag.add(BlockRegistry.SKYLAND_OOW_RUNESTONE.get());
        tag.add(BlockRegistry.SKYLAND_FALL_RUNESTONE.get());
        BlockRegistry.GOD_RUNESTONES.forEach(deferredBlock -> tag.add(deferredBlock.get()));
    }
}
