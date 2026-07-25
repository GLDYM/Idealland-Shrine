package cn.mcmod.ideallandshrine.datagen;

import cn.mcmod.ideallandshrine.IdeallandShrine;
import cn.mcmod.ideallandshrine.init.block.BlockRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class IdeallandBlockStateProvider extends BlockStateProvider {
    public IdeallandBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, IdeallandShrine.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        registerCubeAll(BlockRegistry.SKYLAND_DULL_RUNESTONE.get());
        registerCubeAll(BlockRegistry.SKYLAND_OOW_RUNESTONE.get());
        registerCubeAll(BlockRegistry.SKYLAND_FALL_RUNESTONE.get());
        simpleBlockWithItem(BlockRegistry.TELEPORTER_DIM_C16.get(), models().getExistingFile(ResourceLocation.fromNamespaceAndPath(IdeallandShrine.MODID, "block/teleporter_dim_c16")));
        BlockRegistry.GOD_RUNESTONES.forEach(deferredBlock -> registerHorizontalFacingCube(deferredBlock.get()));
    }

    private void registerCubeAll(Block block) {
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
        ModelFile model = models().cubeAll(id.getPath(), ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "block/" + id.getPath()));
        simpleBlockWithItem(block, model);
    }

    private void registerHorizontalFacingCube(Block block) {
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
        ModelFile model = models().cubeAll(id.getPath(), ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "block/" + id.getPath()));
        horizontalBlock(block, model);
        simpleBlockItem(block, model);
    }
}
