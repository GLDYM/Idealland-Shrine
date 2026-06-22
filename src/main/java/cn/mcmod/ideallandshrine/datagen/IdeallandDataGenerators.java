package cn.mcmod.ideallandshrine.datagen;

import cn.mcmod.ideallandshrine.IdeallandShrine;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = IdeallandShrine.MODID)
public final class IdeallandDataGenerators {
    private IdeallandDataGenerators() {
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var output = generator.getPackOutput();
        var existingFileHelper = event.getExistingFileHelper();
        var lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeClient(), new IdeallandBlockStateProvider(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new IdeallandItemModelProvider(output, existingFileHelper));
        generator.addProvider(event.includeServer(), new IdeallandBlockTagsProvider(output, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new IdeallandLootTableProvider(output, lookupProvider));
    }
}
